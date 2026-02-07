package org.firstinspires.ftc.masters.vison.Confusion;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.pedroPathing.Constants;
import org.firstinspires.ftc.masters.vison.AprilTagDetectionPipeline;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Config // Enables FTC Dashboard
@TeleOp(name = "Wruff!")
public class newLeaf extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    // Start defining our camera and april tag processor
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    // Start defining our follower for pedropathing
    private Follower follower;
    private boolean following = false;
    private final Pose TARGET_LOCATION = new Pose(100, 30, Math.toRadians(135));

    // April tag sizing and camera calibration
    double fx = 822.317;
    double fy = 822.317;
    double cx = 319.495;
    double cy = 242.502;
    double tagsize = 0.166; // In meters

    // Tag detection settings
    int numFramesWithoutDetection = 0;
    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;

    // Start setting up the blackboard for keeping pose
    public static final String POSE_KEY = "Pose";

    public void runOpMode() throws InterruptedException {

        // Sets up pedropathing and sets its pose
        follower = Constants.createFollower(hardwareMap);
//        Pose startingPos = (Pose) blackboard.get(POSE_KEY);
//        follower.setStartingPose(startingPos);

        // FTC dashboard telemetry
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // Sets up our camera and its detection pipeline
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
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

        while (opModeIsActive())
        {
            // Sets the list of our detections
            ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();

            // Stops no detection errors
            if(detections != null)
            {

                telemetry.addData("FPS", camera.getFps());
                telemetry.addData("Overhead ms", camera.getOverheadTimeMs());
                telemetry.addData("Pipeline ms", camera.getPipelineTimeMs());

                // Stops empty errors
                if(detections.isEmpty())
                {
                    numFramesWithoutDetection++;
                    if(numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION)
                    {
                        aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW);
                    }
                }
                else
                {
                    numFramesWithoutDetection = 0;
                    if(detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS)
                    {
                        aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH);
                    }
                    for(AprilTagDetection detection : detections)
                    {

                        Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

                        if(gamepad1.a){
                            float angle = 0;
                            if (detection.id == 20)
                                angle = rot.firstAngle;
                            double distance = detection.pose.z * 39.37008;

                            if (!following) {
                                follower.followPath(
                                        follower.pathBuilder()
                                                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(angle), 0.8))
                                                .build()
                                );
                            }
                        }

                        telemetry.addData("Detected tag ID", detection.id);
                        telemetry.addData("Translation Z CM", detection.pose.z*100);
                        telemetry.addData("Rotation Yaw Degrees", rot.firstAngle);
                    }
                }
                telemetry.update();
            }
        }
    }
}

