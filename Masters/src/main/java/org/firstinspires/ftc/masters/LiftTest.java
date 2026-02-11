package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config // Enables FTC Dashboard
@TeleOp(name = "liftTest")
@Disabled

public class LiftTest extends LinearOpMode {


    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;


    static double upPos = 90.0;
    static


    Servo leftServo = hardwareMap.servo.get("leftServo");



    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                leftServo.setDirection(upPos);
            } else {
                leftServo.setDirection(downPos);
            }
        }
    }
}

