package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.TeleEx.Monitor;

@Config // Enables FTC Dashboard
@TeleOp(name = "FileTesting")
public class doesNothing extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Init init = new Init(hardwareMap);
        Monitor monitor = new Monitor(init, telemetry);

        waitForStart();

        while (opModeIsActive()) {


        }
    }
}

