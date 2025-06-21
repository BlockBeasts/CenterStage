package org.firstinspires.ftc.masters.components;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Init {

    GoBildaPinpointDriver pinpoint = null;

    private final DcMotorEx leftFrontMotor = null;
    private final DcMotorEx rightFrontMotor = null;
    private final DcMotorEx leftRearMotor = null;
    private final DcMotorEx rightRearMotor = null;

    private final DcMotorEx intakeExtendo = null;
    private final Servo intakeArm = null, intakeChain = null;
    private final CRServo intake = null;

    private final DcMotorEx outtakeSlideFront, outtakeSlideBack, outtakeSlideMiddle;

    private final Servo led = null, claw = null;
    private final Servo armPosition = null, clawPosition = null;
    private final Servo pusherServo = null;
    private final CRServo hangLeft, hangRight;
    private final RevColorSensorV3 color = null;

    private final VoltageSensor controlHubVoltageSensor = null;
    private final VoltageSensor expansionHubVoltageSensor = null;

    private final LynxModule controlHublynx = null;
    private final LynxModule expansionHublynx = null;

//    private GamepadEx gp1 = null;

    public Telemetry telemetry;

    public Init(HardwareMap hardwareMap) {
        // Read from the hardware maps
//        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
//        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
//        leftRearMotor = hardwareMap.get(DcMotorEx.class, "backLeft");
//        rightRearMotor = hardwareMap.get(DcMotorEx.class, "backRight");
//
//        // Set the drive motor direction:
//        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
//        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
//        leftRearMotor.setDirection(DcMotor.Direction.REVERSE);
//        rightRearMotor.setDirection(DcMotor.Direction.FORWARD);
//
//        // Don't use the encoders for motor odometry
//        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        // Engage the brakes when the robot cuts off power to the motors
//        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize intake motors and servos
//        claw = hardwareMap.servo.get("claw");
//        armPosition = hardwareMap.servo.get("angleLeft");
//        clawPosition = hardwareMap.servo.get("angleRight");
//
//        intake = hardwareMap.get(CRServo.class, "intake");
//        intakeExtendo = hardwareMap.get(DcMotorEx.class, "intakeExtendo");
//        intakeExtendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        intakeExtendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        intakeArm = hardwareMap.servo.get("intakeLeft");
//        intakeChain = hardwareMap.servo.get("intakeRight");

        outtakeSlideFront = hardwareMap.get(DcMotorEx.class, "vertSlideFront");
        outtakeSlideMiddle = hardwareMap.get(DcMotorEx.class, "vertSlideMiddle");
        outtakeSlideBack = hardwareMap.get(DcMotorEx.class, "vertSlideBack");
        outtakeSlideMiddle.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        color = hardwareMap.get(RevColorSensorV3.class, "color");
//        pusherServo = hardwareMap.servo.get("pusher");

        hangLeft = hardwareMap.get(CRServo.class, "hangLeft");
        hangRight = hardwareMap.get(CRServo.class, "hangRight");
//
//        // Strange and evil devices
//        led = hardwareMap.servo.get("led");
//        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

//        controlHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
//        expansionHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Expansion Hub 2");

//        controlHublynx = hardwareMap.get(LynxModule.class, "Control Hub");
//        expansionHublynx = hardwareMap.get(LynxModule.class, "Expansion Hub 2");

//        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
//
//        for (LynxModule hub : allHubs) {
//            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
//        }

    }

    public DcMotorEx getLeftFrontMotor(){return leftFrontMotor;}
    public DcMotorEx getRightFrontMotor(){return rightFrontMotor;}
    public DcMotorEx getLeftRearMotor(){return leftRearMotor;}
    public DcMotorEx getRightRearMotor(){return rightRearMotor;}

    public CRServo getIntake() {
        return intake;
    }

    public DcMotorEx getIntakeExtendo() {
        return intakeExtendo;
    }

    public Servo getIntakeArm() {
        return intakeArm;
    }

    public Servo getIntakeChain() {
        return intakeChain;
    }

    public DcMotorEx getOuttakeSlideFront() {
        return outtakeSlideFront;
    }

    public DcMotorEx getOuttakeSlideBack() {
        return outtakeSlideBack;
    }

    public DcMotorEx getOuttakeSlideMiddle() {
        return outtakeSlideMiddle;
    }

    public Servo getClaw() {
        return claw;
    }

    public Servo getArmPosition() {
        return armPosition;
    }

    public Servo getClawPosition() {
        return clawPosition;
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

    public Servo getLed() {
        return led;
    }

    public CRServo getHangLeft() {return hangLeft;}
    public CRServo getHangRight() {return hangRight;}

    public GoBildaPinpointDriver getPinpoint() { return pinpoint; }

    public VoltageSensor getControlHubVoltageSensor() { return controlHubVoltageSensor; }
    public VoltageSensor getExpansionHubVoltageSensor() { return expansionHubVoltageSensor; }

    public LynxModule getControlHublynx() { return controlHublynx; }
    public LynxModule getExpansionHublynx() {  return expansionHublynx; }

//    public void setGamePad(Gamepad gp){
//        gp1 = new GamepadEx(gp);
//    }
//
//    public GamepadEx getGp1() {
//        return gp1;
//    }

}