package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config // Enables FTC Dashboard
@Autonomous(name = "auto-redwall")

public class tankauto_redwall extends LinearOpMode {


    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;
    int targetSpeed = 3000;

    double realSpeed = 0;


    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public void runOpMode() throws InterruptedException {

        double speedTPS= (double) (28 * targetSpeed) / 60;
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

            shoot.setVelocity(speedTPS);


            shootBall();
            shootBall();
            shootBall();






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
