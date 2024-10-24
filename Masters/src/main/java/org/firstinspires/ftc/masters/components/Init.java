package org.firstinspires.ftc.masters.components;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Init {

    private final DcMotor leftFrontMotor;
    private final DcMotor rightFrontMotor;
    private final DcMotor leftRearMotor;
    private final DcMotor rightRearMotor;

    private final DcMotor extendSlide;
    private final DcMotor rotateSlide;

    private final Servo slideServo1, slideServo2, stringServo;
    private final DcMotor wheelMotor;
    BNO055IMU imu;

    public Telemetry telemetry;

    public Init(HardwareMap hardwareMap) {
        // Read from the hardware maps
        leftFrontMotor = hardwareMap.dcMotor.get("frontLeft");
        rightFrontMotor = hardwareMap.dcMotor.get("frontRight");
        leftRearMotor = hardwareMap.dcMotor.get("backLeft");
        rightRearMotor = hardwareMap.dcMotor.get("backRight");

        extendSlide = hardwareMap.dcMotor.get("backLeft");
        rotateSlide = hardwareMap.dcMotor.get("backRight");

        // Reset the encoder values
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set the drive motor direction:
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        rightRearMotor.setDirection(DcMotor.Direction.FORWARD);

        // Don't use the encoders for motor odometry
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Engage the brakes when the robot cuts off power to the motors
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extendSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotateSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = null;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Initialize intake motors and servos
        wheelMotor = hardwareMap.dcMotor.get("wheelMotor");
        slideServo1 = hardwareMap.servo.get("slideServo1");
        slideServo2 = hardwareMap.servo.get("slideServo2");
        stringServo = hardwareMap.servo.get("stringServo");
    }

    public DcMotor getLeftFrontMotor(){return leftFrontMotor;}
    public DcMotor getRightFrontMotor(){return rightFrontMotor;}
    public DcMotor getLeftRearMotor(){return leftRearMotor;}
    public DcMotor getRightRearMotor(){return rightRearMotor;}

    public DcMotor getExtendSlide(){return extendSlide;}
    public DcMotor getRotateSlide(){return rotateSlide;}

    public DcMotor getWheelMotor(){return wheelMotor;}
    public Servo getSlideServo1(){return slideServo1;}
    public Servo getSlideServo2(){return slideServo2;}
    public Servo getStringServo(){return stringServo;}

}