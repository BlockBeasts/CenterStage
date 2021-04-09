package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="testShooterLoading", group = "Test")
public class TestShooterLoading extends LinearOpMode{
    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot= new RobotClass(hardwareMap, telemetry, this);

        waitForStart();
        PIDFCoefficients pidfCoefficients= robot.getShooterMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("pidf", pidfCoefficients.toString());
        robot.shooterServo1(.7);
        robot.shooterServo1(.7);
        robot.pause(5000);

    }
}

