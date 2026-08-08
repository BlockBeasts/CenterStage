package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
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

@Config
@Autonomous(name = "far auto red LOBSTER")

public class farAutoRedCV extends LinearOpMode {

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

    private final Pose startPose = new Pose(144-56, 8.8, Math.toRadians(90));
    private final Pose scorePose = new Pose(88.6, 17.4, Math.toRadians(67.9));

    private final Pose scorePose2 = new Pose( 88, 20, Math.toRadians(68));
    private final Pose scorePose3 = new Pose (88, 20, Math.toRadians(68));

    private final Pose pickupPlayer = new Pose (144-24, 13, Math.toRadians(0 ));
    private final Pose endPickupPlayer = new Pose (144-14, 13, Math.toRadians(-10));

    private final Pose endPose = new Pose (144-34, 12, Math.toRadians(90)); // need to change values to get off the line

    private PathChain scorePreload;
    private PathChain  score1,  score2, end, pickupFromBox, pickupFromBoxEnd, score3;

    public enum State {Start, ToTag,  ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3,End};
    private State pathState;

    int scored = 0;

    double run = 1;
    double pick = 1;

    ElapsedTime elapsedTime = null;
    ElapsedTime shootWait =null;
    ElapsedTime reverseWait = null;

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";

    public static final String POSE_KEY = "Pose";

    public Lift lift;
    private ElapsedTime stuckTimer = new ElapsedTime();

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

        while (init.getPinpoint().getDeviceStatus() != GoBildaPinpointDriver.DeviceStatus.READY){
            init.getPinpoint().update();
            telemetry.addData("Pinpoint Status", init.getPinpoint().getDeviceStatus());
            telemetry.update();
            sleep(500);
        } if (init.getPinpoint().getDeviceStatus() == GoBildaPinpointDriver.DeviceStatus.READY){
            init.getPinpoint().update();
            telemetry.addData("Pinpoint Status", init.getPinpoint().getDeviceStatus());
            telemetry.update();
        }

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

            outake.update(0, 0);
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
                follower.followPath(scorePreload);
                pathState= State.ToGoal;

                break;
            case ToGoal:
                if(!follower.isBusy() && init.getShooterLeft().getVelocity()>1880) {

                    if (beforeShoot){
                        outake.shootAll();
                        shootWait = new ElapsedTime();
                        beforeShoot = false;

                    } else {
                        if (shootWait!=null && shootWait.milliseconds()>500){
                            shootWait =null;
                            beforeShoot = true;
                            if (scored == 0 || scored ==1 || scored ==2) {
                                intake.intakeOn();
                                follower.followPath(pickupFromBox, run, false);
                                pathState = State.ToSpike;
                            } else {
                                follower.followPath(end, run, true);
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
                    follower.followPath(pickupFromBoxEnd, pick, false);
                    pathState= State.Pickup;

                }
                break;
            case Pickup:
                if (follower.isBusy()){
                    double currentVelocity = follower.getVelocity().getMagnitude();
                    if (currentVelocity<0.3){
                        if (stuckTimer.seconds()>0.75){
                            follower.breakFollowing();
                            stuckTimer.reset();
                        }
                    } else {
                        stuckTimer.reset();
                    }
                }
                if(!follower.isBusy()) {
                    if (elapsedTime==null){
                        elapsedTime = new ElapsedTime();
                    }
                    if (elapsedTime.milliseconds()>2000) {

                        if (scored == 0 ) {
                            follower.followPath(score1, pick, false);
                        } else if (scored ==1){
                            follower.followPath(score2, pick, false);
                        } else if (scored ==2){
                            follower.followPath(score3, pick, false);
                        }

                        scored++;
                        pathState = State.ToGoal;
                        elapsedTime= null;
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
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading(), 0.8)
                .build();

        pickupFromBox = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickupPlayer))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickupPlayer.getHeading(), 0.8)
                .build();

        pickupFromBoxEnd = follower.pathBuilder()
                .addPath(new BezierLine(pickupPlayer, endPickupPlayer))
                .setLinearHeadingInterpolation(pickupPlayer.getHeading(), endPickupPlayer.getHeading())
                .build();

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(endPickupPlayer, scorePose))
                .setLinearHeadingInterpolation(endPickupPlayer.getHeading(), scorePose.getHeading(), 0.8)
                .build();


        score2 = follower.pathBuilder()
                .addPath(new BezierLine(endPickupPlayer, scorePose2))
                .setLinearHeadingInterpolation(endPickupPlayer.getHeading(), scorePose2.getHeading(), 0.5)
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(endPickupPlayer, scorePose3))
                .setLinearHeadingInterpolation(endPickupPlayer.getHeading(), scorePose3.getHeading(), 0.5)
                .build();

        end = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, endPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), endPose.getHeading())
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
