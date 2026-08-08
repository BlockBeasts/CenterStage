package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Outake;

@Config // Enables FTC Dashboard
//@TeleOp(name = "Starter")
@Disabled
public class keepItUp extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    Init init;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        init = new Init(hardwareMap);

        waitForStart();

        Servo left = init.getOutakeTrayLeft();
        Servo middle = init.getOutakeTrayMiddle();
        Servo right = init.getOutakeTrayRight();

        while (opModeIsActive()) {

        }
    }
}

