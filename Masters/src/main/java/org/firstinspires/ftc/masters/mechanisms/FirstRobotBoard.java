package org.firstinspires.ftc.masters.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FirstRobotBoard {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor intakeMotor;
    private Servo flowerLeftServo;
    private Servo flowerRightServo;

    public void init(HardwareMap hwmap) {
        frontLeft = hwmap.get(DcMotor.class, "frontLeft");
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight = hwmap.get(DcMotor.class, "frontRight");
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft = hwmap.get(DcMotor.class, "backLeft");
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight = hwmap.get(DcMotor.class, "backRight");
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //intake
        intakeMotor = hwmap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        flowerLeftServo = hwmap.get(Servo.class, "flowerLeftServo");
        flowerRightServo = hwmap.get(Servo.class, "flowerRightServo");
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

        frontLeft.setPower(leftFrontPower);
        backLeft.setPower(leftRearPower);
        frontRight.setPower(rightFrontPower);
        backRight.setPower(rightRearPower);
    }

    public void extractFlowers(double position) {
        flowerLeftServo.setPosition(position);
        flowerRightServo.setPosition(position);
    }
    public void intakeControl(double speed) {
        intakeMotor.setPower(speed);
    }
}