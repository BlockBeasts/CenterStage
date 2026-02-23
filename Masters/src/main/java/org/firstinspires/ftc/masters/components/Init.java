package org.firstinspires.ftc.masters.components;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Init {

    GoBildaPinpointDriver pinpoint = null;
    DcMotorEx frontLeft, backLeft, frontRight, backRight;
    DcMotorEx shooterLeft, shooterRight;
    DcMotorEx intakeMotor;
    DcMotor lift;
    Servo outakeTrayLeft, outakeTrayRight, outakeTrayMiddle;
    Servo leftLight, middleLight, rightLight;
    Servo hoodLeftServo, hoodRightServo;
    RevColorSensorV3 colorLeft, colorMiddle, colorRight;

    public static double p=240;
    public static double f=13;

    public Init(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        shooterLeft = hardwareMap.get(DcMotorEx.class, "leftMotor");
        shooterRight = hardwareMap.get(DcMotorEx.class, "rightMotor");


        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");


        lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outakeTrayLeft = hardwareMap.get(Servo.class, "trayLeftServo");
        outakeTrayMiddle = hardwareMap.get(Servo.class, "trayMiddleServo");
        outakeTrayRight = hardwareMap.get(Servo.class, "trayRightServo");

        hoodLeftServo = hardwareMap.get(Servo.class, "leftServo");
        hoodRightServo = hardwareMap.get(Servo.class, "rightServo");

        leftLight = hardwareMap.get(Servo.class, "leftLed");
        middleLight = hardwareMap.get(Servo.class, "middleLed");
        rightLight = hardwareMap.get(Servo.class, "rightLed");

        colorLeft = hardwareMap.get(RevColorSensorV3.class, "leftColor");
        colorMiddle = hardwareMap.get(RevColorSensorV3.class, "middleColor");
        colorRight = hardwareMap.get(RevColorSensorV3.class, "rightColor");

        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        outakeTrayLeft.setPosition(Constant.leftTrayBottom);
        outakeTrayMiddle.setPosition(Constant.middleTrayBottom);
        outakeTrayRight.setPosition(Constant.rightTrayBottom);

        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients coef = new PIDFCoefficients(p,0,0,f);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coef);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");
        pinpoint.resetPosAndIMU();

    }

    public DcMotorEx getFrontLeft() {
        return frontLeft;
    }

    public DcMotorEx getBackLeft() {
        return backLeft;
    }

    public DcMotorEx getFrontRight() {
        return frontRight;
    }

    public DcMotorEx getBackRight() {
        return backRight;
    }

    public DcMotorEx getShooterLeft() {
        return shooterLeft;
    }

    public DcMotorEx getShooterRight() {
        return shooterRight;
    }

    public DcMotorEx getIntakeMotor() {
        return intakeMotor;
    }

    public DcMotor getLift() {
        return lift;
    }

    public Servo getOutakeTrayLeft() {
        return outakeTrayLeft;
    }

    public Servo getOutakeTrayRight() {
        return outakeTrayRight;
    }

    public Servo getOutakeTrayMiddle() {
        return outakeTrayMiddle;
    }

    public Servo getLeftLight() {
        return leftLight;
    }

    public Servo getMiddleLight() {
        return middleLight;
    }

    public Servo getRightLight() {
        return rightLight;
    }

    public Servo getHoodLeftServo() {
        return hoodLeftServo;
    }

    public Servo getHoodRightServo() {
        return hoodRightServo;
    }

    public RevColorSensorV3 getColorLeft() {
        return colorLeft;
    }

    public RevColorSensorV3 getColorMiddle() {
        return colorMiddle;
    }

    public RevColorSensorV3 getColorRight() {
        return colorRight;
    }

    public GoBildaPinpointDriver getPinpoint() { return pinpoint; }

}