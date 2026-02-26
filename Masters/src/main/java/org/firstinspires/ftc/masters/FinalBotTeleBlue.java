package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

    boolean lifted = false;

    boolean outakeToggle = true;
    boolean intakeRevToggle = false;

    boolean intakeToggle = false;

    boolean debounceLeft = false;

    boolean debounceRight = false;

    private final Pose startPose = new Pose(72, 0, Math.toRadians(90));

    public void initializeHardwareAlliance(){

        allianceColor = Constant.AllianceColor.BLUE;

    }


    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub: allHubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


        init = new Init(hardwareMap);
        initializeHardwareAlliance();

        outake = new Outake(init, telemetry, allianceColor);
        intake = new Intake(init, outake, telemetry);

        lift = new Lift(init);

        waitForStart();

        outake.startShooter();

        while (opModeIsActive()) {

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
