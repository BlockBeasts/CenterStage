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
    private DcMotor launcherMotorLeft;
    private DcMotor launcherMotorRight;

    public void init(HardwareMap hwmap) {
        frontLeft = hwmap.get(DcMotor.class, "frontLeft");
        frontRight = hwmap.get(DcMotor.class, "frontRight");
        backLeft = hwmap.get(DcMotor.class, "backLeft");
        backRight = hwmap.get(DcMotor.class, "backRight");
        //set mode
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //intake
        intakeMotor = hwmap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //set drive motor direction
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        //flower servos
        flowerLeftServo = hwmap.get(Servo.class, "flowerLeftServo");
        flowerRightServo = hwmap.get(Servo.class, "flowerRightServo");
        //launch motors
        launcherMotorLeft = hwmap.get(DcMotor.class, "launcherMotorLeft");
        launcherMotorRight = hwmap.get(DcMotor.class, "launcherMotorRight");
        launcherMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        launcherMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
    public void launchProjectiles(double position) {
        launcherMotorLeft.setTargetPosition(position);
        launcherMotorRight.setTargetPosition(position);
    }
}