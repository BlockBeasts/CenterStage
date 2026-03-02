package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
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
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Config
@Autonomous(name = "goal auto red")

public class spike3AutoRedCV extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

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

    private final Pose startPose = new Pose(121.5, 120, Math.toRadians(126));

    private final Pose tagPose = new Pose(100, 110, Math.toRadians(90));

    private final Pose scorePose = new Pose(86.5, 101, Math.toRadians(34));
    private final Pose pickup1Pose = new Pose(96, 86.5, Math.toRadians(-36)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose endPickup1 = new Pose (122, 86.5, Math.toRadians(-36));
    private final Pose pickup2Pose = new Pose(96, 86.5-24, Math.toRadians(-36)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose endPickup2 = new Pose(122, 86.5-24, Math.toRadians(-36));
    private final Pose pickup3Pose = new Pose(96, 86.5-48, Math.toRadians(-36)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose endPickup3 = new Pose(122, 86.5-48, Math.toRadians(-36));

    private final Pose evilScore =  new Pose(90, 90, Math.toRadians(40));

    private final Pose endPose = new Pose (144-60, 85, Math.toRadians(180-135)); // need to change values to get off the line

    private PathChain scorePreload, readTag;
    private PathChain spike1, pickup1, score1, spike2, pickup2, score2, spike3, pickup3, score3, end;

    public enum State {Start, ToTag,  ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3,End};
    private State pathState;

    int scored = 0;

    double run = 0.95;
    double pick = 0.5;

    ElapsedTime elapsedTime = null;
    ElapsedTime shootWait =null;
    ElapsedTime reverseWait = null;

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";

    public static final String POSE_KEY = "Pose";

    public Lift lift;

    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init, telemetry, Constant.AllianceColor.RED);
        intake = new Intake(init, outake, telemetry);
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

        telemetry.setMsTransmissionInterval(50);

        pathState = State.Start;
        int tagId = -1;

        //sleep(5500);

        while (opModeIsActive()){

            if (tagId==-1) {
                ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();
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
            telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
            telemetry.update();

            outake.update();

//            if (outake.has3Balls()){
//                intake.intakeReverse();
//            }

        }

    }

    public void autonomousPathUpdate(int tagId) {
        switch (pathState) {
            case Start:

                follower.followPath(readTag);
                pathState = State.ToTag;

                break;
            case ToTag:
                if (!follower.isBusy()){
                    outake.startShooter();
                    follower.followPath(scorePreload);
                    pathState = State.ToGoal;
                }
                break;
            case ToGoal:
                if(!follower.isBusy()) {

                    if (elapsedTime ==null){
                        reverseWait = null;
                        elapsedTime= new ElapsedTime();
                        lift.lowerBot();
                        //intake.intakeOn();
                    } else if (elapsedTime.milliseconds()>750) {
                        outake.shootAll();

                        if (shootWait ==null) {
                            shootWait = new ElapsedTime();

                        }
                        if (shootWait!=null && shootWait.milliseconds()>100){
                            lift.liftBot();
                        }
                        if ( shootWait!=null && shootWait.milliseconds() > 800) {
                            elapsedTime = null;
                            shootWait = null;
                            lift.stopLift();

                            if (scored == 0) {
                                intake.intakeOn();
                                follower.followPath(spike1, run, false);
                                pathState = State.ToSpike;
                            } else if (scored == 1) {
                                intake.intakeOn();
                                follower.followPath(spike2, run, false);
                                pathState = State.ToSpike;
                            } else if (scored == 2) {
                                intake.intakeOn();
                                follower.followPath(spike3, run, false);
                                pathState = State.ToSpike;
                            } else {
                                follower.followPath(end);
                                pathState = State.End;
                            }
                        }
                    }

//                } else {
//                    if (reverseWait==null){
//                        reverseWait = new ElapsedTime();
//                    } else if (reverseWait.milliseconds()>3500){
//                        intake.intakeOff();
//                    } else if (reverseWait.milliseconds()>2500){
//                        intake.intakeReverse();
//                    }
                }

                break;
            case ToSpike:
                if(!follower.isBusy()) {
                    //pick up
                    intake.intakeOn();
                    if (scored == 0){
                        intake.intakeOn();
                        follower.followPath(pickup1, pick, false);
                    } else if (scored ==1){
                        intake.intakeOn();
                        follower.followPath(pickup2, pick, false);
                    } else if (scored ==2 ){
                        intake.intakeOn();
                        follower.followPath(pickup3, pick, false);
                    }
                    pathState= State.Pickup;

                }
                break;
            case Pickup:
                if(!follower.isBusy()) {
                    //intake.intakeReverse();
                    //elapsedTime = new ElapsedTime();
                    if (scored == 0){
                        follower.followPath(score1, run, false);
                    } else if (scored ==1){
                        follower.followPath(score2, run, false);
                    } else if (scored ==2 ){
                        follower.followPath(score3, run, false);
                    }
                    scored++;
                    pathState = State.ToGoal;

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

        readTag = follower.pathBuilder()
                .addPath (new BezierLine(startPose, tagPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), tagPose.getHeading())
                .build();

        scorePreload= follower.pathBuilder()
                .addPath(new BezierLine(tagPose, scorePose))
                .setLinearHeadingInterpolation(tagPose.getHeading(), scorePose.getHeading())
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
                .addPath(new BezierLine(endPickup1, evilScore))
                .setLinearHeadingInterpolation(endPickup1.getHeading(), evilScore.getHeading())
                .build();

        spike2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath( new BezierLine(pickup2Pose, endPickup2))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), endPickup2.getHeading())
                .build();

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, evilScore))
                .setLinearHeadingInterpolation(endPickup2.getHeading(), evilScore.getHeading())
                .build();

        spike3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath( new BezierLine(pickup3Pose, endPickup3))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), endPickup3.getHeading())
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, evilScore))
                .setLinearHeadingInterpolation(endPickup3.getHeading(), evilScore.getHeading())
                .build();

        end = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
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


}
