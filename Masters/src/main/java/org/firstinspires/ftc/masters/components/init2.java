package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class init2 {

    DcMotorEx frontLeft;
    DcMotorEx backLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;

    DcMotorEx intake;

    DcMotorEx outakeLeft;

    DcMotorEx outakeMiddle;

    DcMotorEx outakeRight;

    Servo outakeLiftLeft;

    Servo outakeLiftRight;

    Servo outakeLiftMiddle;

    Servo leftLight;
    Servo middleLight;
    Servo rightLight;

    public void Initrobot(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        intake = hardwareMap.get(DcMotorEx.class, "intake");


        outakeLeft = hardwareMap.get(DcMotorEx.class, "outakeLeft");
        outakeMiddle = hardwareMap.get(DcMotorEx.class, "outakeMiddle");
        outakeRight = hardwareMap.get(DcMotorEx.class, "outakeRight");

        outakeLiftLeft = hardwareMap.get(Servo.class, "outakeLiftLeft");
        outakeLiftMiddle = hardwareMap.get(Servo.class, "outakeLiftMiddle");
        outakeLiftRight = hardwareMap.get(Servo.class, "outakeLiftRight");

        leftLight = hardwareMap.get(Servo.class, "leftLight");
        middleLight = hardwareMap.get(Servo.class, "middleLight");
        rightLight = hardwareMap.get(Servo.class, "rightLight");
        
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
}
