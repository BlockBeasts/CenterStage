package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;



@Config // Enables FTC Dashboard
@TeleOp(name = "tank-drive")
public class tankDrive extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    double forward_power = 0;
    double turn_power = 0;

    double shootpower = 0;
    static double speedUpCoef = 0.05;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRight = hardwareMap.dcMotor.get("backRight");

        DcMotor shoot = hardwareMap.dcMotor.get("shooter");
        CRServo pusher1 = hardwareMap.crservo.get("pusher1");
        CRServo pusher2 = hardwareMap.crservo.get("pusher2");

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
                if (shootpower != 1) {
                    shootpower += speedUpCoef;
                }

            } else if (gamepad2.dpad_down) {
                if (shootpower != 0) {
                    shootpower -= speedUpCoef;
                }
            }

            //todo add push servo stuff, here!



            forward_power = gamepad1.left_stick_y * -1;
            turn_power = (gamepad1.left_trigger + (gamepad1.right_trigger * -1) * -1);

            frontLeft.setPower(forward_power + turn_power);
            frontRight.setPower(forward_power - turn_power);
            backLeft.setPower(forward_power + turn_power);
            backRight.setPower(forward_power - turn_power);

            shoot.setPower(shootpower);

        }
    }
}

