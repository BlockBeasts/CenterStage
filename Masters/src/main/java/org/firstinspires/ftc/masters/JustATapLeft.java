package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Config // Enables FTC Dashboard
@Autonomous(name = "justatapleft")


public class JustATapLeft extends LinearOpMode{

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        while (opModeIsActive()) {

            frontLeft.setPower(.5);
            backLeft.setPower(.5);
            frontRight.setPower(-.5);
            backRight.setPower(-.5);
            sleep(1000);
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
            sleep(1000);

        }
    }
}
