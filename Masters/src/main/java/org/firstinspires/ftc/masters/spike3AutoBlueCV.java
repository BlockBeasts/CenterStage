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
@Autonomous(name = "goal auto blue")

public class spike3AutoBlueCV extends LinearOpMode {

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

    private final Pose startPose = new Pose(26, 130, Math.toRadians(55));

    private final Pose tagPose = new Pose (55, 100, Math.toRadians(90));

    private final Pose scorePose = new Pose(56, 88, Math.toRadians(135));
    private final Pose pickup1Pose = new Pose(65, 64, Math.toRadians(150)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose endPickup1 = new Pose (40, 64, Math.toRadians(150));
    private final Pose pickup2Pose = new Pose(65, 43, Math.toRadians(150)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose endPickup2 = new Pose(40, 43, Math.toRadians(150));
    private final Pose pickup3Pose = new Pose(65, 24, Math.toRadians(150)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose endPickup3 = new Pose(40, 24, Math.toRadians(150));

    private final Pose evilScore = new Pose(56, 88, Math.toRadians(145));

    private final Pose endPose = new Pose (60, 85, Math.toRadians(135)); // need to change values to get off the line

    private PathChain scorePreload, readTag;
    private PathChain spike1, pickup1, score1, spike2, pickup2, score2, spike3, pickup3, score3, end;

    public enum State {Start, ToTag,  ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3,End};
    private State pathState;

    int scored = 0;

    double run = 1;
    double pick = 0.4;

    ElapsedTime elapsedTime = null;
    ElapsedTime shootWait =null;

    public static final String POSE_KEY = "Pose";

    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init, telemetry, Constant.AllianceColor.BLUE);
        intake = new Intake(init, outake, telemetry);

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

            if(!follower.isBusy()) {
                blackboard.put(POSE_KEY, follower.getPose());
            }

            // Feedback to Driver Hub for debugging
            telemetry.addData("tagId", tagId);
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
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
                            elapsedTime= new ElapsedTime();
                        } else if (elapsedTime.milliseconds()>2000) {
                            outake.shootAll();
                            if (shootWait ==null) {
                                shootWait = new ElapsedTime();
                                intake.intakeReverse();
                            }
                            if ( shootWait!=null && shootWait.milliseconds() > 500) {
                                elapsedTime = null;
                                shootWait = null;

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
                   elapsedTime = new ElapsedTime();
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
                .addPath(new BezierLine(startPose, pickup1Pose))
                .setLinearHeadingInterpolation(startPose.getHeading(), pickup1Pose.getHeading())
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
                .addPath(new BezierLine(pickup2Pose, scorePose))
                .setLinearHeadingInterpolation(endPickup2.getHeading(), scorePose.getHeading())
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
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(endPickup3.getHeading(), scorePose.getHeading())
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
