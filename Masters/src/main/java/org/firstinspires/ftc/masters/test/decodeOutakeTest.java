package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Config // Enables FTC Dashboard
@TeleOp(name = "DecodeOutakeTest")
public class decodeOutakeTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static float ServoPos = 0;

    public static float liftLeftServoPos = 0;
    public static float liftRightServoPos = 0;
    public static float liftMiddleServoPos = 0;


    public static int MotorVol = 0;
    public static int IntakeSpeed = 0;

    Servo leftServo, rightServo, liftLeftServo, liftRightServo, liftMiddleServo;
    DcMotorEx leftMotor, rightMotor, intakeMotor;

    public void runOpMode() throws InterruptedException {

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");

        liftLeftServo = hardwareMap.get(Servo.class, "liftLeftServo");
        liftRightServo = hardwareMap.get(Servo.class, "liftRightServo");
        liftMiddleServo = hardwareMap.get(Servo.class, "liftMiddleServo");

        leftMotor = hardwareMap.get(DcMotorEx.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotorEx.class, "rightMotor");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.dpadUpWasPressed()){
                MotorVol = MotorVol + 100;
            }
            if(gamepad1.dpadDownWasPressed()){
                MotorVol = MotorVol - 100;
            }

            if(gamepad1.dpadLeftWasPressed()){
                ServoPos = ServoPos + 10;
            }
            if(gamepad1.dpadRightWasPressed()){
                ServoPos = ServoPos - 10;
            }

            if (gamepad1.left_bumper) {
                liftLeftServoPos = 90f;
                liftMiddleServoPos = 90f;
                liftRightServoPos= 90f;
            }
            if (gamepad1.right_bumper) {
                liftLeftServoPos = 0f;
                liftMiddleServoPos = 0f;
                liftRightServoPos= 0f;
            }



            if(gamepad1.aWasPressed()){
                IntakeSpeed = 1;
            } else if (gamepad1.bWasPressed()) {
                IntakeSpeed = -1;
            } else if (gamepad1.xWasPressed()){
                IntakeSpeed = 0;
            }

            leftMotor.setVelocity(MotorVol);
            rightMotor.setVelocity(MotorVol);
            intakeMotor.setPower(IntakeSpeed);

            liftLeftServo.setPosition(liftLeftServoPos);
            liftMiddleServo.setPosition(liftMiddleServoPos);
            liftRightServo.setPosition(liftRightServoPos);


            leftServo.setPosition(ServoPos);
            rightServo.setPosition(ServoPos);

            telemetry.addData("Current Servo Pos: ", ServoPos);
            telemetry.addData("Current Motor Vol: ", MotorVol);
            telemetry.addData("Actual Vel: ", leftMotor.getVelocity());
            telemetry.update();
        }
    }
}

