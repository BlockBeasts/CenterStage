package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Hang {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    PIDController pidController;
    public static double p = 0, i = 0, d = 0;
    public static int target;

    Init init;
    Telemetry telemetry;

    DcMotorEx outtakeSlideFront, outtakeSlideBack, outtakeSlideMiddle;
    CRServo hangLeft, hangRight;

    public Hang(Init init, Telemetry telemetry){

        this.telemetry = telemetry;
        this.init = init;

        outtakeSlideFront = init.getOuttakeSlideFront();
        outtakeSlideMiddle = init.getOuttakeSlideMiddle();
        outtakeSlideBack = init.getOuttakeSlideBack();

        hangLeft = init.getHangLeft();
        hangRight = init.getHangRight();

        initializeHardware();

    }

    public void initializeHardware() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        pidController = new PIDController(p, i, d);
        pidController.setPID(p, i, d);

    }

    public void update(){

        int rotatePos = outtakeSlideMiddle.getCurrentPosition();
        double pid = pidController.calculate(rotatePos, target);

        outtakeSlideFront.setPower(pid);
        outtakeSlideBack.setPower(pid);
        outtakeSlideMiddle.setPower(pid);

    }

    public void setTarget(int target){

        this.target = target;

    }

    public void servoEnable(){

        hangLeft.setPower(1);
        hangRight.setPower(1);

    }

    public void servoReverse(){

        hangLeft.setPower(-1);
        hangRight.setPower(-1);

    }

    public void servoDisable(){

        hangLeft.setPower(0);
        hangRight.setPower(0);

    }

}
