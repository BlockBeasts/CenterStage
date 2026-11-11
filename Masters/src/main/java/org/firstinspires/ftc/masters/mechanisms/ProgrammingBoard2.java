package org.firstinspires.ftc.masters.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ProgrammingBoard2 {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor BackLeft;
    private DcMotor BackRight;
    private DigitalChannel touchSensor;

    public void init(HardwareMap hwmap) {
        FrontLeft = hwmap.get(DcMotor.class, "FrontLeft");
        FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRight = hwmap.get(DcMotor.class, "FrontRight");
        FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeft = hwmap.get(DcMotor.class, "BackLeft");
        BackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackRight = hwmap.get(DcMotor.class, "BackRight");
        BackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    public void cartesianDrive(double x, double y, double t) {

        if (Math.abs(y) < 0.2) {
            y = 0;
        }
        if (Math.abs(x) < 0.2) {
            x = 0;
        }

        double leftFrontPower = y + x + t;
        double leftRearPower = y - x + t;
        double rightFrontPower = y - x - t;
        double rightRearPower = y + x - t;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(t), 1);

        leftFrontPower /= denominator;
        leftRearPower /= denominator;
        rightFrontPower /= denominator;
        rightRearPower /= denominator;

        FrontLeft.setPower(leftFrontPower);
        BackLeft.setPower(leftRearPower);
        FrontRight.setPower(rightFrontPower);
        BackRight.setPower(rightRearPower);
    }

}