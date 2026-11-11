package org.firstinspires.ftc.masters.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class ProgrammingBoard1 {
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
    public void powerAllMotors(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        double maxSpeed = 1.0;
        maxSpeed = Math.max(maxSpeed, Math.abs(frontLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(frontRightPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backRightPower));

        frontLeftPower /= maxSpeed;
        frontRightPower /= maxSpeed;
        backLeftPower /= maxSpeed;
        backRightPower /= maxSpeed;

        FrontLeft.setPower(frontLeftPower);
        FrontRight.setPower(frontRightPower);
        BackLeft.setPower(backLeftPower);
        BackRight.setPower(backRightPower);
    }
    public void runDriveForward(double speed, double strafe, double rotate) {
        double frontLeftPower = speed + strafe + rotate;
        double frontRightPower = speed - strafe - rotate;
        double backLeftPower = speed - strafe + rotate;
        double backRightPower = speed + strafe - rotate;

        powerAllMotors(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }
    public void powerFrontLeft(double speed) {
        FrontLeft.setPower(speed);
    }
    public void powerFrontRight(double speed) {
        FrontRight.setPower(speed);
    }
    public void powerBackLeft(double speed) {
        BackLeft.setPower(speed);
    }
    public void powerBackRight(double speed) {
        BackRight.setPower(speed);
    }
}
