package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config // Enables FTC Dashboard
@TeleOp(name = "intakeDrawTest")
@Disabled
public class intakeDrawTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public void runOpMode() throws InterruptedException {

        DcMotorEx shoota = hardwareMap.get(DcMotorEx.class, "shoota");
        DcMotorEx shootb = hardwareMap.get(DcMotorEx.class, "shootb");
        long sum = 0;
        long cycle = 0;
        double avg = 0;

        // 1+ 1
        // 1 +1

        // 1+1

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            sum = (long) (sum + shoota.getCurrent(CurrentUnit.AMPS));
            cycle++;
            avg = (double) sum / cycle;
            telemetry.addData("Avg Current", avg);
            telemetry.update();

            if (gamepad1.dpad_up){
                shoota.setPower(1);
                shootb.setPower(1);
            } else if (gamepad1.dpad_down){
                shoota.setPower(-1);
                shootb.setPower(-1);
            } else {
                shoota.setPower(0);
                shootb.setPower(0);
            }
        }
    }
}

