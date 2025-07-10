package org.firstinspires.ftc.masters.usedTests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.masters.components.Init;

@TeleOp(group="Hardware Values", name = "OuttakeTest")
@Config
public class OuttakeTest extends LinearOpMode {

    public static double armPos = 0.4;
    public static double clawPos = 0.65;
    public static double clawSnippy = 0.3;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Init init = new Init(hardwareMap);

        Servo armPosition = init.getArmPosition();
        Servo clawPosition = init.getClawPosition();
        Servo claw = init.getClaw();

        waitForStart();

        while (opModeIsActive()) {

        if (gamepad1.dpad_left){
            armPosition.setPosition(armPos);
        }
        // .15 is transfer
        // 1 is back
        if (gamepad1.dpad_up){
            clawPosition.setPosition(clawPos);
        }
        // waiting for metal part
        // 0.65 is straight up
        // 0.9 is bent towards intake at 90
        // 0.4 is bent towards outtake at 90
        if (gamepad1.dpad_right){
            claw.setPosition(clawSnippy);
        }
        // 0.3 is open
        // 0.58 is close

            // Scoring
            // arm pos .53
            // claw pos .9
            // clas .58

            // wall
            // arm pos .95
            // claw pos .15
            // claw .3


        }
    }
}

