package org.firstinspires.ftc.masters.usedTests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.masters.components.Init;

@Config
@TeleOp(group="Hardware Values", name = "IntakeTest")
public class IntakeTest extends LinearOpMode {

    public static double intakeArmPos = 0.5;
    public static double intakeChainPos = 0.5;
    public static double intakePower = 0;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Init init = new Init(hardwareMap);

        Servo intakeArm = init.getIntakeArm();
        Servo intakeChain = init.getIntakeChain();
        CRServo intake = init.getIntake();

        waitForStart();

        while (opModeIsActive()) {

        if (gamepad1.dpad_left){
            intakeArm.setPosition(intakeArmPos);
        }
        // .55 is ground
        // 0 for spec
        // .3 for transfer

        if (gamepad1.dpad_up){
            intakeChain.setPosition(intakeChainPos);
        }
        // 0 is fully back
        // .8 is straight front

        if (gamepad1.dpad_right){
            intake.setPower(intakePower);
        }

        }
    }
}

