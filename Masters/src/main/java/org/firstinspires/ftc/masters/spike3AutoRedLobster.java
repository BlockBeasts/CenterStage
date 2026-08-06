package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.components.Constant;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.pedroPathing.Constants;
import org.firstinspires.ftc.masters.vison.AprilTagDetectionPipeline;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Config
@Autonomous(name = "goal red LOBSTER")

public class spike3AutoRedLobster extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    public static int offsetNear = -30;
    public static int offsetFar = -50;
    static final double FEET_PER_METER = 3.28084;

    double fx = 822.317;
    double fy = 822.317;
    double cx = 319.495;
    double cy = 242.502;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int numFramesWithoutDetection = 0;

    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;

    private Follower follower;
    private final Pose startPose = new Pose(125, 120, Math.toRadians(129));

    private final Pose tagPose = new Pose(100, 110, Math.toRadians(90));

    private final Pose scorePose = new Pose(92, 101, Math.toRadians(50));
    private final Pose pickup1Pose = new Pose(144-49, 86, Math.toRadians(0)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose endPickup1 = new Pose (144-30 , 86, Math.toRadians(0));

    private final Pose gatePoint = new Pose(125, 66, Math.toRadians(15));
    private final Pose intermediate = new Pose (144-50, 72, Math.toRadians(30));
    private final Pose pickup2Pose = new Pose(144-55, 61, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose endPickup2 = new Pose(144-23, 61, Math.toRadians(0));

    private final Pose pickup3Pose = new Pose(144-49, 86-46, Math.toRadians(0)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose endPickup3 = new Pose(144-21, 86-46, Math.toRadians(0));
    private final Pose evilScore = new Pose(90, 82, Math.toRadians(40));

    private final Pose scoreLast  = new Pose(90, 82, Math.toRadians(40));

    private final Pose endPose = new Pose (60, 85, Math.toRadians(135)); // need to change values to get off the line

    private PathChain scorePreload;
    private PathChain spike1, pickup1, score1, spike2, pickup2, score2, spike3, pickup3, score3, end, toGate, gateToScore, pickup2ToGate;

    public enum State {Start, ToTag,  ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3, Gate, End};
    private State pathState;

    int scored = 0;
    double run = 1;
    double pick = 1;

    ElapsedTime elapsedTime = null;
    ElapsedTime shootWait =null;
    ElapsedTime reverseWait = null;
    ElapsedTime gateWait = null;

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";
    public Lift lift;

    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init, telemetry, Constant.AllianceColor.RED);
        intake = new Intake(init, outake, telemetry);
        outake.setIntake(intake);
        lift = new Lift(init);

        follower = Constants.createFollower(hardwareMap);
        outake.setFollower(follower);
        buildPaths();
        follower.setStartingPose(startPose);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });



        waitForStart();

        telemetry.setMsTransmissionInterval(100);


        pathState = State.Start;
        int tagId = -1;

        //sleep(5500);

        while (opModeIsActive()){
            telemetry.addData("status", init.getPinpoint().getDeviceStatus());
            ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();

            if (tagId==-1) {
                tagId = getTag(detections);
            }

            // These loop the movements of the robot, these must be called continuously in order to work
            follower.update();
            autonomousPathUpdate(tagId);

            blackboard.put(POSE_KEY_X, follower.getPose().getX());
            blackboard.put(POSE_KEY_Y, follower.getPose().getY());
            blackboard.put(POSE_KEY_H, follower.getPose().getHeading());
//            telemetry.addData("saved pos x", blackboard.get(POSE_KEY_X));
//            telemetry.addData("saved pos y", blackboard.get(POSE_KEY_Y));
//            telemetry.addData("saved pos h", blackboard.get(POSE_KEY_H));

            // Feedback to Driver Hub for debugging
            telemetry.addData("tagId", tagId);
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.addData("velocity", init.getShooterLeft().getVelocity());
            telemetry.addData("lift", lift.getCurrentPosition());
            telemetry.update();

            outake.update(offsetNear, offsetFar);
            lift.update();

//            if (outake.has3Balls()){
//                intake.intakeReverse();
//            }

        }

    }

    boolean beforeShoot = true;

    public void autonomousPathUpdate(int tagId) {
        switch (pathState) {
            case Start:
                outake.startShooter();
                follower.followPath(scorePreload, false);
                pathState = State.ToGoal;

                break;

            case ToGoal:
                if(!follower.isBusy()) {
                    reverseWait = null;
                    intake.intakeOn();

                    if (beforeShoot){

                        if (lift.getCurrentPosition()<Constant.liftShootLimit){
                            lift.liftRobot();
                        } else {
                            outake.shootAll();
                            if (shootWait ==null) {
                                shootWait = new ElapsedTime();
                                beforeShoot = false;
                            }
                        }
                    } else {
                        if (shootWait!=null && shootWait.milliseconds()>250){
                            this.lift.lowerBot();
                        }
                        if (lift.getCurrentPosition()<Constant.liftDriveLimit){
                            shootWait =null;
                            beforeShoot = true;
                            if (scored == 0) {
                                intake.intakeOn();
                                follower.followPath(spike2, run, false);
                                pathState = State.ToSpike;
                            } else if (scored == 1) {
                                intake.intakeOn();
                                follower.followPath(spike3, 1, false);
                                pathState = State.ToSpike;
                            } else if (scored == 2) {
                                intake.intakeOn();
                                follower.followPath(spike1, run, false);
                                pathState = State.ToSpike;
                            } else {
                                follower.followPath(end);
                                pathState = State.End;
                            }
                        }
                    }

                } else {
                    if (reverseWait==null){
                        reverseWait = new ElapsedTime();
//                    } else if (reverseWait.milliseconds()>3000){
//                        intake.intakeOn();
                    } else if (reverseWait.milliseconds()>500){
                        intake.intakeReverse();
                    }
                }

                break;
            case ToSpike:
                if(!follower.isBusy()) {
                    //pick up
                    intake.intakeOn();
                    if (scored == 0){
                        intake.intakeOn();
                        follower.followPath(pickup2, pick, false);
                    } else if (scored ==1){
                        intake.intakeOn();
                        follower.followPath(pickup3, pick, false);
                    } else if (scored ==2 ){
                        intake.intakeOn();
                        follower.followPath(pickup1, pick, false);
                    }
                    pathState= State.Pickup;

                }
                break;
            case Pickup:
                if(!follower.isBusy()) {
                    //intake.intakeReverse();
                    //elapsedTime = new ElapsedTime();
                    if (scored == 0){
                        follower.followPath(pickup2ToGate, run, false);
                        elapsedTime= new ElapsedTime();
                        pathState = State.Gate;
                    } else if (scored ==1){
                        follower.followPath(score3, run, false);
                        pathState = State.ToGoal;
                    } else if (scored ==2 ){
                        follower.followPath(score1, run, false);
                        pathState = State.ToGoal;
                    }
                    scored++;


                }
                break;
            case Gate:
                if (elapsedTime.milliseconds()>700 && elapsedTime.milliseconds()<1000){
                    intake.intakeReverse();
                } else if (elapsedTime.milliseconds()>1000){
                    intake.intakeOff();
                }
                if (!follower.isBusy()){
                    if (gateWait ==null){
                        gateWait = new ElapsedTime();
                    }
                    if (gateWait!=null && gateWait.milliseconds()>1300){
                        follower.followPath(gateToScore, run, true);
                        pathState = State.ToGoal;
                    }
                }

                break;
            case End:
                if (!follower.isBusy()){
                    intake.intakeOff();
                    outake.stopShooter();
                }
        }
    }

    public void buildPaths() {


        scorePreload= follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        spike1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        pickup1 = follower.pathBuilder()
                .addPath( new BezierLine(pickup1Pose, endPickup1))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), endPickup1.getHeading())
                .build();

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(endPickup1, scoreLast))
                .setLinearHeadingInterpolation(endPickup1.getHeading(), scoreLast.getHeading())
                .build();

        toGate = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, gatePoint))
                .setLinearHeadingInterpolation(scorePose.getHeading(), gatePoint.getHeading())
                .build();

        gateToScore = follower.pathBuilder()
                .addPath(new BezierCurve(gatePoint, new Pose(144-54, 66), evilScore))
                .setLinearHeadingInterpolation(gatePoint.getHeading(), evilScore.getHeading())
                .build();

        spike2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath( new BezierLine(pickup2Pose, endPickup2))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), endPickup2.getHeading())
                .build();

        pickup2ToGate = follower.pathBuilder()
                .addPath(new BezierLine(endPickup2, gatePoint))
                .setLinearHeadingInterpolation(endPickup2.getHeading(), gatePoint.getHeading())
                .build();

//
//        score2 = follower.pathBuilder()
//                .addPath(new BezierLine(gatePoint, intermediate))
//                .addPath(new BezierLine(intermediate, evilScore))
//                .setLinearHeadingInterpolation(gatePoint.getHeading(), evilScore.getHeading())
//                .build();

        spike3 = follower.pathBuilder()
                .addPath(new BezierLine(evilScore, pickup3Pose))
                .setLinearHeadingInterpolation(evilScore.getHeading(), pickup3Pose.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath( new BezierLine(pickup3Pose, endPickup3))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), endPickup3.getHeading())
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(endPickup3, evilScore))
                .setLinearHeadingInterpolation(endPickup3.getHeading(), evilScore.getHeading())
                .build();

        end = follower.pathBuilder()
                .addPath(new BezierLine(evilScore, pickup2Pose))
                .setLinearHeadingInterpolation(evilScore.getHeading(), pickup2Pose.getHeading())
                .build();
    }

    protected int getTag(ArrayList<AprilTagDetection> detections){

        if (detections!=null) {
            for (AprilTagDetection tag : detections) {
                if (tag.id == 21 || tag.id == 22 || tag.id == 23) {
                    return tag.id;
                }
            }
        }

        return -1;
    }

    protected AprilTagPose getPose(ArrayList<AprilTagDetection> detections){
        if (detections!=null){
            for (AprilTagDetection tag: detections){
                if (tag.id==24){
                    return  tag.pose;
                }
            }
        }
        return null;
    }


}
