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

import org.firstinspires.ftc.masters.components.Constant;
import org.firstinspires.ftc.masters.components.Init;

@Config // Enables FTC Dashboard
@TeleOp(name = "DecodeOutakeTest")
@Disabled
public class decodeOutakeTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static float ServoPos = 0;

    public static double liftLeftServoPos = 0;
    public static double liftRightServoPos = 0;
    public static double liftMiddleServoPos = 0;


    public static int MotorVol = 0;
    public static int IntakeSpeed = 0;



    public void runOpMode() throws InterruptedException {

       Init init = new Init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {



            if (gamepad1.left_bumper) {
                liftLeftServoPos = Constant.leftTrayTop;
                liftMiddleServoPos = Constant.middleTrayTop;
                liftRightServoPos= Constant.rightTrayTop;
            }
            if (gamepad1.right_bumper) {
                liftLeftServoPos = Constant.leftTrayBottom;
                liftMiddleServoPos = Constant.middleTrayBottom;
                liftRightServoPos= Constant.rightTrayBottom;
            }



            if(gamepad1.aWasPressed()){
                IntakeSpeed = 1;
            } else if (gamepad1.bWasPressed()) {
                IntakeSpeed = -1;
            } else if (gamepad1.xWasPressed()){
                IntakeSpeed = 0;
            }

            init.getShooterLeft().setVelocity(MotorVol);
            init.getShooterRight().setVelocity(MotorVol);
//            intakeMotor.setPower(IntakeSpee
            init.getOutakeTrayLeft().setPosition(liftLeftServoPos);
            init.getOutakeTrayMiddle().setPosition(liftMiddleServoPos);
            init.getOutakeTrayRight().setPosition(liftRightServoPos);



            telemetry.addData("Current Servo Pos: ", ServoPos);
            telemetry.addData("Current Motor Vol: ", MotorVol);
            telemetry.addData("Actual Vel: ", init.getShooterLeft().getVelocity());
            telemetry.update();
        }
    }
}

