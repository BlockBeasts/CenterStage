package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
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
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(name = "far auto blue")

public class farAutoBlueCV extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;

    private VisionPortal visionPortal;


    private AprilTagProcessor aprilTag;



    static final double FEET_PER_METER = 3.28084;

    double fx = 822.317;
    double fy = 822.317;
    double cx = 319.495;
    double cy = 242.502;

    // UNITS ARE METERS
    double tagsize = 0.166;

    private static final boolean USE_WEBCAM = true;

    int numFramesWithoutDetection = 0;

    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;

    private Follower follower;
    private final Pose startPose = new Pose(56, 8.8, Math.toRadians(90));
    private final Pose scorePose = new Pose(55.3, 20.4, Math.toRadians(112));

    private final Pose pickupPlayer = new Pose (34, 14, Math.toRadians(180 ));
    private final Pose pickupPlayer2 = new Pose (10.8, 11.1, Math.toRadians(-161));
    private final Pose pickup2Pose = new Pose(49, 86-28, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose endPickup2 = new Pose(20, 86-28, Math.toRadians(180));
    private final Pose pickup3Pose = new Pose(49, 86-48, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose endPickup3 = new Pose(20, 86-48, Math.toRadians(180));
    private final Pose evilScore = new Pose(144-90, 80, Math.toRadians(150));

    private final Pose endPose = new Pose (34, 12, Math.toRadians(90)); // need to change values to get off the line

    private PathChain scorePreload, readTag;
    private PathChain spike1, pickup1, score1, spike2, pickup2, score2, spike3, pickup3, score3, end, pickupFromBox, pickupFromBoxEnd;

    public enum State {Start, ToTag,  ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3,End};
    private State pathState;

    int scored = 0;

    double run = 1;
    double pick = 0.6;

    double turnTo = 0;

    ElapsedTime turnCutOff = null;
    ElapsedTime elapsedTime = null;
    ElapsedTime shootWait =null;
    ElapsedTime reverseWait = null;



    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";
    public Lift lift;



    public void runOpMode() throws InterruptedException {
        initAprilTag();

        init = new Init(hardwareMap);
        outake = new Outake(init, telemetry, Constant.AllianceColor.BLUE);
        intake = new Intake(init, outake, telemetry);
        outake.setIntake(intake);
        lift = new Lift(init);

        follower = Constants.createFollower(hardwareMap);
        outake.setFollower(follower);
        buildPaths();
        follower.setStartingPose(startPose);


        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        telemetry.setMsTransmissionInterval(50);

        pathState = State.Start;
        int tagId = -1;

        //sleep(5500);

        while (opModeIsActive()){


//            if (tagId==-1) {
//                ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();
//                tagId = getTag(detections);
//            }

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
            telemetry.update();

            outake.update();

//            if (outake.has3Balls()){
//                intake.intakeReverse();
//            }

            if (turnCutOff != null) {
                if (turnCutOff.milliseconds() > 500){
                    follower.breakFollowing();
                    turnCutOff = null;
                }

            }

        }

    }

    boolean beforeShoot = true;

    public void autonomousPathUpdate(int tagId) {
        switch (pathState) {
            case Start:
                outake.startShooter();
                follower.followPath(scorePreload);
                pathState= State.ToGoal;

                break;
            case ToGoal:
                if(!follower.isBusy() && init.getShooterLeft().getVelocity()>100) {

                    if (beforeShoot){

                        aimToGoal();

                        if (turnCutOff == null) {
                            outake.shootAll();
                            if (shootWait ==null) {
                                shootWait = new ElapsedTime();
                                beforeShoot = false;
                            }
                        }


                    } else {
                        if (shootWait!=null && shootWait.milliseconds()>500){

                            shootWait =null;
                            beforeShoot = true;
                            if (scored == 0) {
                                intake.intakeOn();
                                follower.followPath(pickupFromBox, run, false);
                                pathState = State.ToSpike;
                            } else
//
                            {
                                follower.followPath(end, run, true);
                                pathState = State.End;
                            }
                           // }
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
                        follower.followPath(pickupFromBoxEnd, pick, false);
                    }
                    pathState= State.Pickup;

                }
                break;
            case Pickup:
                if(!follower.isBusy()) {
                    //intake.intakeReverse();
                    //elapsedTime = new ElapsedTime();
                    if (scored == 0){
                        follower.followPath(score1, pick, false);
//                    } else if (scored ==1){
//                        follower.followPath(score2, run, false);
//                    } else if (scored ==2 ){
//                        follower.followPath(score3, run, false);
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


        scorePreload= follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        pickupFromBox = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickupPlayer))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickupPlayer.getHeading())
                .build();

        pickupFromBoxEnd = follower.pathBuilder()
                .addPath(new BezierLine(pickupPlayer, pickupPlayer2))
                .setLinearHeadingInterpolation(pickupPlayer.getHeading(), pickupPlayer2.getHeading())
                .build();

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(pickupPlayer2, scorePose))
                .setLinearHeadingInterpolation(pickupPlayer2.getHeading(), scorePose.getHeading())
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
                .addPath(new BezierLine(scorePose, endPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), endPose.getHeading())
                .build();
    }

    private void initAprilTag() {

        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, aprilTag);
        }

    }


    protected void aimToGoal() {
        List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> currentDetections = aprilTag.getDetections();


        double currentTag = getRotations(currentDetections);
        telemetry.addData("current heading", currentTag);

        if (currentTag !=0) {

            turnTo = Math.toDegrees(follower.getPose().getHeading())+currentTag;
            follower.turn(Math.toRadians(currentTag));
            turnCutOff = new ElapsedTime();


        }
    }



    protected double getRotations(List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> detections){

        if (detections!=null) {
            for (org.firstinspires.ftc.vision.apriltag.AprilTagDetection tag : detections) {
                if (tag.id == 20) {

                    return tag.ftcPose.bearing;
                }
            }
        }

        return 0;
    }


}
