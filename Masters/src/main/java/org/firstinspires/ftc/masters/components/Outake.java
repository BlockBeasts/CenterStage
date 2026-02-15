package org.firstinspires.ftc.masters.components;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outake {
    Init init;

    Telemetry telemetry;
    public Outake(Init init) {
        this.init= init;
    }

    public static int motorVel = 1000;

    public static double hoodServoPos = 0;

    public static double liftLeftPos = 0;
    public static double liftRightPos = 0;
    public static double liftMiddlePos = 0;

    public static int delay =500;

    ElapsedTime shootLeftDelay = null;
    ElapsedTime shootRightDelay = null;
    ElapsedTime shootMiddleDelay = null;



    public void update() {

        init.getShooterLeft().setVelocity(motorVel);
        init.getShooterRight().setVelocity(motorVel);

        if (shootLeftDelay!=null && shootLeftDelay.milliseconds()>delay){
            liftLeftPos = Constant.leftTrayBottom;
            shootLeftDelay = null;
        }
        if (shootMiddleDelay!=null && shootMiddleDelay.milliseconds()>delay){
            liftMiddlePos = Constant.middleTrayBottom;
            shootMiddleDelay = null;
        }
        if (shootRightDelay!=null && shootRightDelay.milliseconds()>delay){
            liftRightPos = Constant.rightTrayBottom;
            shootRightDelay = null;
        }

        init.outakeTrayLeft.setPosition(liftLeftPos);
        init.outakeTrayMiddle.setPosition(liftMiddlePos);
        init.outakeTrayRight.setPosition(liftRightPos);



        init.getHoodLeftServo().setPosition(hoodServoPos);
        init.getHoodRightServo().setPosition(hoodServoPos);
    }
    public void shootGreen() {
        liftLeftPos = 0.65;
        liftMiddlePos = .90;
        liftRightPos = 0.35;
    }

    public void shootLeft(){
        liftLeftPos = Constant.leftTrayTop;
        shootLeftDelay = new ElapsedTime();
    }

    public void shootMiddle(){
        liftMiddlePos = Constant.middleTrayTop;
        shootMiddleDelay = new ElapsedTime();
    }

    public void setShootRightDelay(){
        liftRightPos = Constant.rightTrayTop;
        shootRightDelay = new ElapsedTime();
    }

    public void shootAll(){
        liftLeftPos = Constant.leftTrayTop;
        liftMiddlePos = Constant.middleTrayTop;
        liftRightPos = Constant.rightTrayTop;
        shootLeftDelay = new ElapsedTime();
        shootRightDelay = new ElapsedTime();
        shootMiddleDelay = new ElapsedTime();
    }
    public void reset() {
        liftLeftPos = Constant.leftTrayBottom;
        liftMiddlePos = Constant.middleTrayBottom;
        liftRightPos = Constant.rightTrayBottom;
        shootLeftDelay = null;
        shootRightDelay = null;
        shootMiddleDelay = null;
    }






}