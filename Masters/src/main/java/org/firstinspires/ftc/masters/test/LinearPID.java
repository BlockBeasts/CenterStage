package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config // Enables FTC Dashboard
@TeleOp(name = "LinearPID")
public class LinearPID extends LinearOpMode {

    //TODO: https://www.youtube.com/watch?v=E6H6Nqe6qJo

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    public final double ticks_in_degree = 537.7 / 180;

    private DcMotor motor;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.dcMotor.get("changeme");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {

            controller.setPID(p, i, d);
            int slidePos = motor.getCurrentPosition();
            double pid = controller.calculate(slidePos, target);
            double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

            double power = pid + ff;

            motor.setPower(power);

            telemetry.addData("pos ", slidePos);
            telemetry.addData("target ", target);
            telemetry.update();

        }
    }
}

