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

@TeleOp
@Config
public class PidTune extends OpMode {

    private PIDController controller;

    public static double p = 0.0009, i = 0, d = 0.00001;

    public static int target = 0;

    DcMotorEx outtakeSlideFront, outtakeSlideBack, outtakeSlideMiddle;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        outtakeSlideFront = hardwareMap.get(DcMotorEx.class, "vertSlideFront");
        outtakeSlideMiddle = hardwareMap.get(DcMotorEx.class, "vertSlideMiddle");
        outtakeSlideBack = hardwareMap.get(DcMotorEx.class, "vertSlideBack");
        outtakeSlideFront.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeSlideBack.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeSlideFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeSlideFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void loop(){

        controller.setPID(p,i,d);
        int rotatePos = -(outtakeSlideFront.getCurrentPosition());
        double pid = controller.calculate(rotatePos, target);

        outtakeSlideFront.setPower(pid);
        outtakeSlideMiddle.setPower(pid);
        outtakeSlideBack.setPower(pid);

        telemetry.addData("ArmPos", rotatePos);
        telemetry.addData("Target", target);
        telemetry.update();

    }

    //50k low bucket
    //100k high
    //63k spec


}

