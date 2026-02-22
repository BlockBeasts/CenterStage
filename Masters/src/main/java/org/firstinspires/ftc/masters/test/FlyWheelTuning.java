package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.masters.components.Init;

@Config
@TeleOp
public class FlyWheelTuning extends OpMode {

    public DcMotorEx shooterLeft, shooterRight;

    public static int highVel= 1500;
    public static int lowVel =0;
    public static int targetVel= highVel;

    public static double p=240;
    public static double f=13;
    public static double d=0;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        Init init = new Init(hardwareMap);
        shooterLeft= init.getShooterLeft();
        shooterRight = init.getShooterRight();

        PIDFCoefficients coef = new PIDFCoefficients(p,0,d,f);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
    }

    @Override
    public void loop() {

        PIDFCoefficients coef = new PIDFCoefficients(p,0,d,f);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);

        shooterLeft.setVelocity(targetVel);
        shooterRight.setVelocity(targetVel);

        double curVelocity= shooterLeft.getVelocity();
        double error= targetVel - shooterLeft.getVelocity();

        telemetry.addData ("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.update();
    }
}
