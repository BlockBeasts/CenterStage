package org.firstinspires.ftc.masters.components;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Init {

    //GoBildaPinpointDriver pinpoint = null;

    private final DcMotorEx leftFrontMotor;
    private final DcMotorEx rightFrontMotor;
    private final DcMotorEx leftRearMotor;
    private final DcMotorEx rightRearMotor;

    private final DcMotorEx intakeExtendo;
    private final Servo intakeArm, intakeChain;
    private final CRServo intake;

    private final DcMotorEx outtakeSlideFront, outtakeSlideBack, outtakeSlideMiddle;

    private final Servo led, claw;
    private final Servo armPosition, clawPosition;
    private final Servo pusherServo;
    private final CRServo hangLeft, hangRight;
    private final RevColorSensorV3 color;

    private final VoltageSensor controlHubVoltageSensor;
    private final VoltageSensor expansionHubVoltageSensor;

    private final LynxModule controlHublynx;
    private final LynxModule expansionHublynx;

    private GamepadEx gp1;

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

        //Initialize intake motors and servos
        claw = hardwareMap.servo.get("claw");
        armPosition = hardwareMap.servo.get("armPosition");
        clawPosition = hardwareMap.servo.get("clawPosition");

        intake = hardwareMap.get(CRServo.class, "intake");
        intakeExtendo = hardwareMap.get(DcMotorEx.class, "intakeExtendo");
        intakeExtendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeArm = hardwareMap.servo.get("intakeArm");
        intakeChain = hardwareMap.servo.get("intakeChain");

        outtakeSlideFront = hardwareMap.get(DcMotorEx.class, "vertSlideFront");
        outtakeSlideMiddle = hardwareMap.get(DcMotorEx.class, "vertSlideMiddle");
        outtakeSlideBack = hardwareMap.get(DcMotorEx.class, "vertSlideBack");
        outtakeSlideFront.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeSlideBack.setDirection(DcMotorSimple.Direction.REVERSE);
       outtakeSlideFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        color = hardwareMap.get(RevColorSensorV3.class, "color");
        pusherServo = hardwareMap.servo.get("pusher");

        hangLeft = hardwareMap.get(CRServo.class, "hangLeft");
        hangRight = hardwareMap.get(CRServo.class, "hangRight");

        // Strange and evil devices
        led = hardwareMap.servo.get("led");
        //pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

        controlHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        expansionHubVoltageSensor = hardwareMap.get(VoltageSensor.class, "Expansion Hub");

        controlHublynx = hardwareMap.get(LynxModule.class, "Control Hub");
        expansionHublynx = hardwareMap.get(LynxModule.class, "Expansion Hub");

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

    }

    public DcMotorEx getLeftFrontMotor(){return leftFrontMotor;}
    public DcMotorEx getRightFrontMotor(){return rightFrontMotor;}
    public DcMotorEx getLeftRearMotor(){return leftRearMotor;}
    public DcMotorEx  getRightRearMotor(){return rightRearMotor;}

    //public GoBildaPinpointDriver getPinpoint() { return pinpoint; }

    public VoltageSensor getControlHubVoltageSensor() { return controlHubVoltageSensor; }
    public VoltageSensor getExpansionHubVoltageSensor() { return expansionHubVoltageSensor; }

    public LynxModule getControlHublynx() { return controlHublynx; }
    public LynxModule getExpansionHublynx() {  return expansionHublynx; }

    public void setGamePad(Gamepad gp){
        gp1 = new GamepadEx(gp);
    }

    public GamepadEx getGp1() {
        return gp1;
    }

}