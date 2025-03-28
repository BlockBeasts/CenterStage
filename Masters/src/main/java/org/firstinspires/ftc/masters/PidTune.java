package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp ()
@Config
public class PidTune extends OpMode {

    private PIDController controller;

    public static double p = 0.0015, i = 0, d = 0;

    public static int target = 0;

    DcMotor outtakeSlideRight, outtakeSlideLeft;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        outtakeSlideRight = hardwareMap.dcMotor.get("vertSlideRight");
        outtakeSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        outtakeSlideLeft= hardwareMap.dcMotor.get("vertSlideLeft");
        outtakeSlideLeft.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void loop(){
        controller.setPID(p,i,d);
        int rotatePos = -(outtakeSlideRight.getCurrentPosition());
        double pid = controller.calculate(rotatePos, target);

        outtakeSlideLeft.setPower(pid);
        outtakeSlideRight.setPower(pid);

        telemetry.addData("ArmPos", rotatePos);
        telemetry.addData("Target", target);
        telemetry.update();

    }


}

