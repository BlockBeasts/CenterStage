package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.masters.components.Init;

@Config // Enables FTC Dashboard@
@TeleOp(name = "Single")
public class SingleLikeMe extends LinearOpMode {

    Init init;

    DcMotorEx frontLeft;
    DcMotorEx backLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;


    public void initializeHardware(){

        frontRight = init.getFrontRight();
        frontLeft = init.getFrontLeft();
        backRight = init.getBackRight();
        backLeft = init.getBackLeft();

    }

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);

        initializeHardware();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.dpad_up){
                frontRight.setPower(1);
            } else if (gamepad1.dpad_left) {
                frontLeft.setPower(1);
            } else if (gamepad1.dpad_right) {
                backRight.setPower(1);
            } else if (gamepad1.dpad_down) {
                backLeft.setPower(1);
            } else {
                frontLeft.setPower(0);
                backLeft.setPower(0);
                frontRight.setPower(0);
                frontLeft.setPower(0);
            }

        }
    }
}

