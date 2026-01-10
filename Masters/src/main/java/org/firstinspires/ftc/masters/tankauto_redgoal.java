package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config // Enables FTC Dashboard
@Disabled
@Autonomous(name = "auto-redgoal")

public class tankauto_redgoal extends LinearOpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    static double Kp = 0.0;
    static double Ki = 0.0;
    static double Kd = 0.0;
    double reference = 0.0;

    double integralSum = 0.0;

    double lastError = 0.0;
    int encoderPosition = 0;

    double error = 0.0;
    double derivative = 0.0;

    double out = 0.0;

    ElapsedTime timer = new ElapsedTime();

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
            encoderPosition = shoot.getCurrentPosition();
            error = reference - encoderPosition;

            derivative = (error - lastError) / timer.seconds();

            integralSum = integralSum + (error * timer.seconds());

            out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

            shoot.setPower(out);
            lastError = error;
        }
    }


}
