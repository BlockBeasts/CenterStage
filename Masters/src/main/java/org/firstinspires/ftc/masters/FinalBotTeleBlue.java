package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.masters.components.Constant;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.masters.pedroPathing.Constants;
import org.firstinspires.ftc.masters.vison.AprilTagDetectionPipeline;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;


@Config // Enables FTC Dashboard
@TeleOp(name = "Decode Teleop Blue")
public class FinalBotTeleBlue extends LinearOpMode {
    protected final FtcDashboard dashboard = FtcDashboard.getInstance();

    Init init;
    Intake intake;
    Outake outake;
    Lift lift;
    DcMotorEx frontLeft;
    DcMotorEx backLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    Constant.AllianceColor allianceColor;
    private Follower follower;

    boolean lifted = false;
    boolean outakeToggle = true;
    boolean debounceLeft = false;
    boolean debounceRight = false;
    private final Pose startPose = new Pose(8.5, 8.5, Math.toRadians(90));

    public void initializeHardwareAlliance(){

        allianceColor = Constant.AllianceColor.BLUE;

    }

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";

    public void runOpMode() throws InterruptedException {

        initAprilTag();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub: allHubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        follower = Constants.createFollower(hardwareMap);

        Double StartX = (Double) blackboard.get(POSE_KEY_X);
        Double StartY = (Double) blackboard.get(POSE_KEY_Y);
        Double StartH = (Double) blackboard.get(POSE_KEY_H);

        Pose genPose = null;
        if (StartX!=null && StartY !=null && StartH !=null) {

            genPose = new Pose(StartX, StartY, StartH);
        }

       // if (genPose != null) {
         //   follower.setStartingPose(genPose);
        //} else {
            follower.setStartingPose(startPose);
        //}
        init = new Init(hardwareMap);
        initializeHardwareAlliance();

        outake = new Outake(init, telemetry, allianceColor);
        outake.setFollower(follower);
        intake = new Intake(init, outake, telemetry);

        lift = new Lift(init);

        frontLeft = init.getFrontLeft();
        frontRight = init.getFrontRight();
        backLeft = init.getBackLeft();
        backRight = init.getBackRight();

        double turnTo = 0;

        waitForStart();

      //  outake.startShooter();

        while (opModeIsActive()) {

            List<org.firstinspires.ftc.vision.apriltag.AprilTagDetection> currentDetections = aprilTag.getDetections();

            blackboard.put(POSE_KEY_X, follower.getPose().getX());
            blackboard.put(POSE_KEY_Y, follower.getPose().getY());
            blackboard.put(POSE_KEY_H,  follower.getPose().getHeading());
            telemetry.addData("saved pos x", follower.getPose().getX());
            telemetry.addData("saved pos y", follower.getPose().getY());
            telemetry.addData("saved pos h",Math.toDegrees(follower.getPose().getHeading()) );

            for (LynxModule hub: allHubs){
                hub.clearBulkCache();
            }

//            if (gamepad2.dpad_right) {
//                intake.intakeOn();
//            }
//            if (gamepad2.dpad_left) {
//                intake.intakeReverse();
//                gamepad2.rumble(100);
//            }
//            if (gamepad2.dpad_up) {
//                intake.intakeOff();
//            }
//            if (gamepad1.dpad_down) {
//                outake.stopShooter();
//                lift.lowerBot();
//            }
//            else if (gamepad1.dpad_up){
//                lift.liftRobot();
//            } else {
//                lift.stopLift();
//            }
////            if (gamepad2.dpad_down){
////
////                if (!outakeToggle) {
////                    outakeToggle = true;
////                    outake.startShooter();
////
////                } else {
////                    outakeToggle = false;
////                    outake.stopShooter();
////                }
////            }
//            if (gamepad2.aWasPressed()) {
//                outake.shootAll();
//            }
//            if (gamepad2.left_stick_x <= -0.9) {
//                outake.shootLeft();
//            }
//            if (gamepad2.left_stick_x >= 0.9) {
//                outake.shootRight();
//            }
//            if (gamepad2.left_stick_y <= -0.9) {
//                outake.shootMiddle();
//            }
//
//            if (gamepad2.left_bumper) {
//                if (!debounceLeft) {
//                    debounceLeft = true;
//                    outake.shootPurple();
//                }
//            } else {
//                debounceLeft = false;
//            }
//
//            if (gamepad2.right_bumper) {
//                if (!debounceRight) {
//                    debounceRight = true;
//                    outake.shootGreen();
//                }
//            } else {
//                debounceRight = false;
//            }
//
//            if(outake.upToSpeed()){
//                gamepad2.setLedColor(0, 255, 0, 750);
//            }
            if (!follower.isBusy()){
                follower.breakFollowing();
            }

            if(gamepad2.startWasPressed()){
                double currentTag = getRotations(currentDetections);
                telemetry.addData("current heading", currentTag);

                if (currentTag !=0) {

                    turnTo = Math.toDegrees(follower.getPose().getHeading())+currentTag;
                    follower.turn(Math.toRadians(currentTag));

                    //follower.turnTo(Math.toRadians(Math.toDegrees(follower.getPose().getHeading())+currentTag));
//                    Path forwards = new Path(new BezierLine(new Pose(0, 0), new Pose(40, 0)));
//                    forwards.setConstantHeadingInterpolation(45);
//
//                    follower.followPath(forwards);
//                    Pose currentPos = follower.getPose();
//                    Pose newPos = new Pose(currentPos.getX() + 1, currentPos.getY() + 1, currentPos.getHeading() + Math.toRadians(currentTag));
//                    if (currentTag != -1) {
//                        follower.followPath(
//                                follower.pathBuilder()
//                                        .addPath(new BezierLine(currentPos, newPos))
//                                        .setLinearHeadingInterpolation(currentPos.getHeading(), currentPos.getHeading() + Math.toRadians(currentTag))
//                                        .build(), 1, false
//                        );
//                    }
                }
            }

            telemetry.addData("turnTo:", turnTo);
            if (currentDetections != null) {
                telemetry.addData("Heading? ", getRotations(currentDetections));
            }

           // intake.update();
           // outake.update();
            follower.update();
            telemetry.update();


            cartesianDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, (gamepad1.right_trigger * .8) - (gamepad1.left_trigger * .8));
        }
    }


    public void cartesianDrive(double x, double y, double t) {

        if (Math.abs(y) < 0.2) {
            y = 0;
        }
        if (Math.abs(x) < 0.2) {
            x = 0;
        }

        double leftFrontPower = y + x + t;
        double leftRearPower = y - x + t;
        double rightFrontPower = y - x - t;
        double rightRearPower = y + x - t;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(t), 1);

        leftFrontPower /= denominator;
        leftRearPower /= denominator;
        rightFrontPower /= denominator;
        rightRearPower /= denominator;

        frontLeft.setPower(leftFrontPower);
        backLeft.setPower(leftRearPower);
        frontRight.setPower(rightFrontPower);
        backRight.setPower(rightRearPower);
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

    }   // end method initAprilTag()


}
