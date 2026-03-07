package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.components.Constant;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.pedroPathing.Constants;
import org.firstinspires.ftc.masters.vison.AprilTagDetectionPipeline;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "Auto Wall Blue TEST")
@Disabled
public class JustATapLeft extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;
    Lift lift;

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

    public enum State {Start, ToShoot, ToPickup, End}
    private int pathState;

    public int moveDistance = 24;

    private final Pose startPose = new Pose(72-8.5, 8.5, Math.toRadians(90));
  //  private final Pose stopPose = new Pose(72-moveDistance, 72+3, Math.toRadians(90));
    private PathChain moveOut;

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";


    public void runOpMode() throws InterruptedException {
        init = new Init(hardwareMap);
        outake = new Outake(init, telemetry, Constant.AllianceColor.BLUE);
        intake = new Intake(init, outake, telemetry);

        follower = Constants.createFollower(hardwareMap);

        outake.setFollower(follower);
       // buildPaths();
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

        setPathState(0);
        follower.turn(Math.toRadians(90));

        while (opModeIsActive()){

            // These loop the movements of the robot, these must be called continuously in order to work
            follower.update();
           // outake.update();
          //  intake.update();

           // autonomousPathUpdate();

            blackboard.put(POSE_KEY_X, follower.getPose().getX());
            blackboard.put(POSE_KEY_Y, follower.getPose().getY());
            blackboard.put(POSE_KEY_H, follower.getPose().getHeading());
//            telemetry.addData("saved pos x", blackboard.get(POSE_KEY_X));
//            telemetry.addData("saved pos y", blackboard.get(POSE_KEY_Y));
//            telemetry.addData("saved pos h", blackboard.get(POSE_KEY_H));

            // Feedback to Driver Hub for debugging
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();

        }

    }

//    public void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                if(!follower.isBusy()) {
//                    follower.followPath(moveOut,true);
//                    setPathState(1);
//                }
//                break;
//            case 1:
//                if(!follower.isBusy()) {
//                    setPathState(-1);
//                }
//                break;
//        }
//    }

    public void setPathState(int pState) {
        pathState = pState;
    }

//    public void buildPaths() {
//
//        moveOut = follower.pathBuilder()
//                .addPath(new BezierLine(startPose, stopPose))
//                .setLinearHeadingInterpolation(startPose.getHeading(), stopPose.getHeading())
//                .build();
//
//    }

}
