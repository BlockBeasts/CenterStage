package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

@Config // Enables FTC Dashboard
@Autonomous(name = "QuickAndDirtyAuto")
public class quickAndDirtyAuto extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;

    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;

    boolean done;

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

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        while (opModeIsActive()) {

            if(!done) {
                shoot.setPower(.6);
                frontLeft.setPower(.5);
                backLeft.setPower(.5);
                frontRight.setPower(.5);
                backRight.setPower(.5);
                sleep(250);
                frontLeft.setPower(0);
                backLeft.setPower(0);
                frontRight.setPower(0);
                backRight.setPower(0);
                sleep(1000);
                for (int i = 0; i < 4; i++) {
                    pusher1.setPower(1);
                    pusher2.setPower(-1);
                    sleep(750);
                    pusher1.setPower(0);
                    pusher2.setPower(0);
                    sleep(4000);
                }
                done = true;
                shoot.setPower(0);
            }
        }
    }
}

