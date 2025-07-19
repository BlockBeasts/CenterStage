package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.components.Hang;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;

@Config // Enables FTC Dashboard
@TeleOp(name = "HangingTest")
public class HangTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    int target = 0;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Init init = new Init(hardwareMap);
        Hang hang = new Hang(init, telemetry);
        Intake intake = new Intake(init, telemetry);

        telemetry.update();

        waitForStart();

        intake.initStatusTeleop();
        intake.pushIn();
        intake.setTarget(0);

        while (opModeIsActive()) {

            if (gamepad1.dpad_up){
                target = target + 100;
            } else if (gamepad1.dpad_down) {
                target = target - 100;
            }

            if (gamepad1.a){
                hang.servoEnable();
            } else if (gamepad1.b) {
                hang.servoReverse();
            } else {
                hang.servoDisable();
            }

            hang.setTarget(target);
            intake.update();
            hang.update();

        }
    }
}

