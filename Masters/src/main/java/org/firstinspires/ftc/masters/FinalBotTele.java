package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;


@Config // Enables FTC Dashboard
@TeleOp(name = "Decode Teleop")
public class FinalBotTele extends LinearOpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();



    Init init;
    Intake intake;
    Outake outake;
    Lift lift;
    DcMotorEx frontLeft;
    DcMotorEx backLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;

    boolean lifted = false;

//    ElapsedTime runtime;

    public void initializeHardware(){

        frontRight = init.getFrontRight();
        frontLeft = init.getFrontLeft();
        backRight = init.getBackRight();
        backLeft = init.getBackLeft();

    }


    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub: allHubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        init = new Init(hardwareMap);
        intake = new Intake(init);
        outake = new Outake(init, telemetry);
        lift = new Lift(init);

        initializeHardware();

        waitForStart();

        outake.unmuteShooter();

        while (opModeIsActive()) {

            for (LynxModule hub: allHubs){
                hub.clearBulkCache();
            }

            if (gamepad2.left_bumper) {
                intake.intakeOn();
            }
            if (gamepad2.dpad_left) {
                intake.intakeReverse();
            }
            if (gamepad2.right_bumper) {
                intake.intakeOff();
            }
            if (gamepad1.dpad_up) {
                lift.liftBot();
            }else if (gamepad1.dpad_down){
                lift.lowerBot();
            } else {
                lift.stopLift();
            }

            if (gamepad2.aWasPressed()) {
                outake.shootAll();
            }
            if (gamepad2.squareWasPressed()) {
                outake.shootLeft();
            }
            if (gamepad2.circleWasPressed()) {
                outake.shootRight();
            }
            if (gamepad2.yWasPressed()) {
                outake.shootMiddle();
            }

//            if (gamepad2.dpad_right) {
//                outake.unmuteShooter();
//            }
//            if (gamepad2.dpad_left) {
//                outake.muteShooter();
//            }

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
