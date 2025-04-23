package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config // Enables FTC Dashboard
@TeleOp
public class Monitor extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Init init = new Init(hardwareMap);
        DriveTrain driveTrain = new DriveTrain(init, telemetry);
        Outtake outtake = new Outtake(init, telemetry);
        Intake intake = new Intake(init, telemetry);

        DcMotorEx leftFrontMotor = init.getLeftFrontMotor();
        DcMotorEx rightFrontMotor = init.getRightFrontMotor();
        DcMotorEx leftRearMotor = init.getLeftRearMotor();
        DcMotorEx rightRearMotor = init.getRightRearMotor();

        waitForStart();

        while (opModeIsActive()) {

            driveTrain.driveNoMultiplier(gamepad1, DriveTrain.RestrictTo.XYT);

            telemetry.addData("LeftFront", leftFrontMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RightFront", rightFrontMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("LeftRear", leftRearMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RightRear", rightRearMotor.getCurrent(CurrentUnit.AMPS));

            telemetry.update();

        }
    }
}

