package org.firstinspires.ftc.masters.components;

import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Init {

    GoBildaPinpointDriver pinpoint;

    private final DcMotorEx leftFrontMotor;
    private final DcMotorEx rightFrontMotor;
    private final DcMotorEx leftRearMotor;
    private final DcMotorEx rightRearMotor;

    private final DcMotorEx intake, intakeExtendo;
    private final Servo intakeLeft, intakeRight;

    private final DcMotorEx outtakeSlideLeft, outtakeSlideRight;

    private final Servo led, claw;
    private final Servo wrist, angleLeft, angleRight, position;
    private final Servo pusherServo;
//    private final Servo ptoRight, ptoLeft, hangLeft, hangRight;
    private final RevColorSensorV3 color;
    private final DigitalChannel breakBeam;
    private IMU imu;

    private final VoltageSensor controlHubVoltageSensor;
    private final VoltageSensor expansionHubVoltageSensor;

    private final LynxModule controlHublynx;
    private final LynxModule expansionHublynx;
    //private final LynxModule servoHublynx;


    public Telemetry telemetry;

    public Init(HardwareMap hardwareMap) {
        // Read from the hardware maps
        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
        leftRearMotor = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightRearMotor = hardwareMap.get(DcMotorEx.class, "backRight");

        // Set the drive motor direction:
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        rightRearMotor.setDirection(DcMotor.Direction.FORWARD);

        // Don't use the encoders for motor odometry
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Engage the brakes when the robot cuts off power to the motors
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize intake motors and servos
        claw = hardwareMap.servo.get("claw");
        position = hardwareMap.servo.get("position");
        wrist = hardwareMap.servo.get("wrist");
        angleLeft = hardwareMap.servo.get("angleLeft");
        angleRight = hardwareMap.servo.get("angleRight");

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeExtendo = hardwareMap.get(DcMotorEx.class, "intakeExtendo");
        //intakeExtendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeExtendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeExtendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeLeft = hardwareMap.servo.get("intakeLeft");
        intakeRight = hardwareMap.servo.get("intakeRight");

        outtakeSlideRight = hardwareMap.get(DcMotorEx.class, "vertSlideRight");


        outtakeSlideLeft = hardwareMap.get(DcMotorEx.class, "vertSlideLeft");
        outtakeSlideLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        color = hardwareMap.get(RevColorSensorV3.class, "color");
        breakBeam = hardwareMap.digitalChannel.get("breakBeam");
        pusherServo = hardwareMap.servo.get("pusher");

        // Strange and evil devices
        led = hardwareMap.servo.get("led");
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

        controlHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        expansionHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Expansion Hub 2");

        controlHublynx = hardwareMap.get(LynxModule.class, "Control Hub");
        expansionHublynx = hardwareMap.get(LynxModule.class, "Expansion Hub 2");
        //servoHublynx = hardwareMap.get(LynxModule.class, "Servo Hub 3");


        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }



    }

    public DcMotorEx getLeftFrontMotor(){return leftFrontMotor;}
    public DcMotorEx getRightFrontMotor(){return rightFrontMotor;}
    public DcMotorEx getLeftRearMotor(){return leftRearMotor;}
    public DcMotorEx getRightRearMotor(){return rightRearMotor;}

    public DcMotorEx getIntake() {
        return intake;
    }

    public DcMotorEx getIntakeExtendo() {
        return intakeExtendo;
    }

    public Servo getIntakeLeft() {
        return intakeLeft;
    }

    public Servo getIntakeRight() {
        return intakeRight;
    }

    public DcMotorEx getOuttakeSlideLeft() {
        return outtakeSlideLeft;
    }

    public DcMotorEx getOuttakeSlideRight() {
        return outtakeSlideRight;
    }

    public Servo getClaw() {
        return claw;
    }

    public Servo getWrist() {
        return wrist;
    }

    public Servo getAngleLeft() {
        return angleLeft;
    }

    public Servo getAngleRight() {
        return angleRight;
    }

    public Servo getPosition() {
        return position;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public Servo getPusherServo() {
        return pusherServo;
    }

    public RevColorSensorV3 getColor() {
        return color;
    }

    public DigitalChannel getBreakBeam() {
        return breakBeam;
    }

    public Servo getLed() {
        return led;
    }

    public GoBildaPinpointDriver getPinpoint() { return pinpoint; }

    public VoltageSensor getControlHubVoltageSensor() { return controlHubVoltageSensor; }
    public VoltageSensor getExpansionHubVoltageSensor() { return expansionHubVoltageSensor; }

    public LynxModule getControlHublynx() { return controlHublynx; }
    public LynxModule getExpansionHublynx() {  return expansionHublynx; }
    //public LynxModule getServoHublynx() { return servoHublynx; }

}