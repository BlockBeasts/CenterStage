package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config // Enables FTC Dashboard
@TeleOp(name = "LinearPID")
public class LinearPID extends LinearOpMode {

    //TODO: https://www.youtube.com/watch?v=E6H6Nqe6qJo

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    private PIDController controller;

    public static double p = 0.09, i = 0, d = 0;
    public static double f = 0.1;

    public static int target = 0;

    public final double ticks_in_degree = 537.7 / 360;

    private DcMotor shoota, shootb;

    public void runOpMode() throws InterruptedException {

        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        shoota = hardwareMap.dcMotor.get("shoota");
        shootb = hardwareMap.dcMotor.get("shootb");
        shoota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shootb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoota.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            controller.setPID(p, i, d);
            int slidePos = shoota.getCurrentPosition();
            double pid = controller.calculate(slidePos, target);
            double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

            double power = pid + ff;

            shoota.setPower(power);
            shootb.setPower(power);
            telemetry.addData("power", power);
            telemetry.addData("pos ", slidePos);
            telemetry.addData("target ", target);
            telemetry.update();

        }
    }
}

