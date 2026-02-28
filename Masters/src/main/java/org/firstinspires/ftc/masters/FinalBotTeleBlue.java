package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
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

    Constant.AllianceColor allianceColor;
    private Follower follower;

    boolean lifted = false;
    boolean outakeToggle = true;
    boolean debounceLeft = false;
    boolean debounceRight = false;
    private final Pose startPose = new Pose(9, 9, Math.toRadians(90));

    public void initializeHardwareAlliance(){

        allianceColor = Constant.AllianceColor.BLUE;

    }

    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";

    public void runOpMode() throws InterruptedException {


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

        if (genPose != null) {
            follower.setStartingPose(genPose);
        } else {
            follower.setStartingPose(startPose);
        }
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
        waitForStart();

        outake.startShooter();

        while (opModeIsActive()) {

            blackboard.put(POSE_KEY_X, follower.getPose().getX());
            blackboard.put(POSE_KEY_Y, follower.getPose().getY());
            blackboard.put(POSE_KEY_H,  follower.getPose().getHeading());
            telemetry.addData("saved pos x", follower.getPose().getX());
            telemetry.addData("saved pos y", follower.getPose().getY());
            telemetry.addData("saved pos h",Math.toDegrees(follower.getPose().getHeading()) );

            for (LynxModule hub: allHubs){
                hub.clearBulkCache();
            }

            if (gamepad2.right_bumper) {
                intake.intakeOn();
            }
            if (gamepad2.dpad_left) {
                intake.intakeReverse();
            }
            if (gamepad2.dpad_up) {
                intake.intakeOff();
            }
            if (gamepad1.dpad_up) {
                outake.stopShooter();
                lift.liftBot();
            }
            else if (gamepad1.dpad_down){
                lift.lowerBot();
            } else {
                lift.stopLift();
            }
            if (gamepad2.dpad_down){

                if (!outakeToggle) {
                    outakeToggle = true;
                    outake.startShooter();

                } else {
                    outakeToggle = false;
                    outake.stopShooter();
                }
            }
            if (gamepad2.aWasPressed()) {
                outake.shootAll();
            }
            if (gamepad2.left_stick_x <= -0.9) {
                outake.shootLeft();
            }
            if (gamepad2.left_stick_x >= 0.9) {
                outake.shootRight();
            }
            if (gamepad2.left_stick_y <= -0.9) {
                outake.shootMiddle();
            }

            if (gamepad2.left_bumper) {
                if (!debounceLeft) {
                    debounceLeft = true;
                    outake.shootPurple();
                }
            } else {
                debounceLeft = false;
            }

            if (gamepad2.right_bumper) {
                if (!debounceRight) {
                    debounceRight = true;
                    outake.shootGreen();
                }
            } else {
                debounceRight = false;
            }

            intake.update();
            outake.update();
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


}
