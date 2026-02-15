package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.components.init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Lift;
import org.firstinspires.ftc.masters.components.Outake2;


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

//    ElapsedTime runtime;

    public void initializeHardware(){

        frontRight = init.getFrontRight();
        frontLeft = init.getFrontLeft();
        backRight = init.getBackRight();
        backLeft = init.getBackLeft();

    }


    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);

        intake = new Intake(init);

        outake = new Outake(init);

        lift = new Lift(init);





        initializeHardware();

        waitForStart();

        while (opModeIsActive()) {



            if (gamepad2.left_bumper) {
                intake.intakeOn();
            }
            if (gamepad2.dpad_left) {
                intake.intakeReverse();
            }
            if (gamepad2.right_bumper) {
                intake.intakeOff();
            }
            if (gamepad2.dpad_up) {
                lift.liftBot();
            }
            if (gamepad2.dpad_down) {
                lift.lowerBot();
            }
            if (gamepad2.aWasPressed()) {
                outake.shootGreen();
            }
            if (gamepad2.bWasPressed()) {
                outake.reset();
            }




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
