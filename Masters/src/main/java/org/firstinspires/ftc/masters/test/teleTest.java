package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.TeleEx.TelemetryEx;

import java.util.ArrayList;
import java.util.List;

@Config // Enables FTC Dashboard
@TeleOp(name = "teleTest")
@Disabled
public class teleTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    TelemetryEx telemetryEx;

    public static double Blank = 0;

    public void runOpMode() throws InterruptedException {

        telemetryEx = new TelemetryEx(this,telemetry);

        List<String> headers = new ArrayList<>();
        headers.add("Filler");
        headers.add("Data");

        telemetryEx.setHeader(headers);

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.a) {
                telemetryEx.addData("Filler", "a");
                telemetryEx.addData("data", "0");
            }

            telemetryEx.update();


        }
    }
}

