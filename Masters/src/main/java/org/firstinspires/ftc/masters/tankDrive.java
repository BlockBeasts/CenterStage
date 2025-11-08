package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
@Config // Enables FTC Dashboard
@TeleOp(name = "tank-drive")
public class tankDrive extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    double forward_power = 0;
    double turn_power = 0;

    double shootpower = 0;

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;
    int targetSpeed = 3000;
    double realSpeed = 0;

    boolean shootActive = false;


    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();


        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        shoot = hardwareMap.get(DcMotorImplEx.class, "shooter");
        pusher1 = hardwareMap.crservo.get("pusher1");
        pusher2 = hardwareMap.crservo.get("pusher2");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad2.dpad_up) { 
                shootpower = 0.6;

            } else if (gamepad2.dpad_down) {
                shootpower = 0;
            }

            if (gamepad2.dpad_right) {
                shootActive = true;
            }
            if (shootActive && realSpeed > targetSpeed) {
                realSpeed = shoot.getVelocity();
            } else if (shootActive) {
                pusher1.setPower(1);
                pusher2.setPower(1);

            }





            forward_power = gamepad1.left_stick_y * -1;
            turn_power = (gamepad1.left_trigger + (gamepad1.right_trigger * -1) * -1);

            frontLeft.setPower(forward_power + turn_power);
            frontRight.setPower(forward_power - turn_power);
            backLeft.setPower(forward_power + turn_power);
            backRight.setPower(forward_power - turn_power);

            shoot.setPower(shootpower);

        }


    }
    public void shootBall(){
        while (realSpeed < targetSpeed && opModeIsActive()) {
            realSpeed = shoot.getVelocity();
        }
        pusher1.setPower(1);
        pusher2.setPower(1);
        ElapsedTime elapsedTime = new ElapsedTime();

        while(elapsedTime.milliseconds() < 2000 && opModeIsActive()) {
            pusher1.setPower(0);
            pusher2.setPower(0);
        }



    }


}

