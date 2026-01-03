package org.firstinspires.ftc.masters;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Outake;



@Config // Enables FTC Dashboard
@TeleOp(name = "ProtoBot_tele")
public class protoBotTele extends LinearOpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Init init;

    Intake intake;
    Outake outake;

    Lift lift;

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;


    public void initializeHardware(){

        frontRight = init.getFrontRight();
        frontLeft = init.getFrontLeft();
        backRight = init.getBackRight();
        backLeft = init.getBackLeft();

    }



    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init);
        intake = new Intake(init);
        initializeHardware();
        outake.init();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                intake.intakeOn();
            }
            if (gamepad1.x) {
                intake.intakeReverse();
            }
            if (gamepad1.b) {
                intake.intakeOff();
            }
            if (gamepad1.dpad_right) {
                outake.launch();
            }
            if (gamepad1.dpad_left) {
                outake.reset();
            }
//            if (gamepad2.dpad_up) {
//                lift.liftBot();
//            }
//            if (gamepad2.dpad_down) {
//                lift.liftBot();
//            }


//            if (gamepad1.dpad_down) {
//                lift.lowerBot();
//            }
//            if (gamepad1.dpad_up) {
//                lift.liftBot();
//            }


            outake.update(telemetry);

            cartesianDrive(Math.pow(gamepad1.left_stick_x, 3), -Math.pow(gamepad1.left_stick_y, 3), Math.pow((gamepad1.right_trigger * .8) - (gamepad1.left_trigger * .8), 3));
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
