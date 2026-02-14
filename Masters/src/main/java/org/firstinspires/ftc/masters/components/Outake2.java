package org.firstinspires.ftc.masters.components;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outake2 {

    public Outake2(Init2 init2) {
        this.init2= init2;
    }

    Init2 init2;


    Telemetry telemetry;

    public static int MotorVol = 1000;

    public static float ServoPos = 0;

    public static float liftLeftServoPos = 0;
    public static float liftRightServoPos = 0;
    public static float liftMiddleServoPos = 0;

    public void update() {

        init2.getleftMotor().setVelocity(MotorVol);
        init2.getrightMotor().setVelocity(MotorVol);


        init2.outakeLiftLeft.setPosition(liftLeftServoPos);
        init2.outakeLiftMiddle.setPosition(liftMiddleServoPos);
        init2.outakeLiftRight.setPosition(liftRightServoPos);


        init2.getleftServoMotor().setPosition(ServoPos);
        init2.getrightServoMotor().setPosition(ServoPos);
    }
    public void shootGreen() {
        liftLeftServoPos = 0.65f;
        liftMiddleServoPos = 90f;
        liftRightServoPos= 0.35f;
    }
    public void reset() {
        liftLeftServoPos = 0f;
        liftMiddleServoPos = 0f;
        liftRightServoPos= 0f;
    }



}