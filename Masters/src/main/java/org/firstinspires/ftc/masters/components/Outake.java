package org.firstinspires.ftc.masters.components;


import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Outake {
    Init init;

    Telemetry telemetry;
    public Outake(Init init, Telemetry telemetry) {
        this.init= init;
        this.telemetry = telemetry;
    }

    public static int motorVel = 1000;

    public static double hoodServoPos = 0;



    public static double liftLeftPos = Constant.leftTrayBottom;
    public static double liftMiddlePos = Constant.middleTrayBottom;
    public static double liftRightPos = Constant.rightTrayBottom;

    private String leftColor = "nothing";
    private String middleColor = "nothing";
    private String rightColor = "nothing";

    public static int delay =500;

    ElapsedTime shootLeftDelay = null;
    ElapsedTime shootRightDelay = null;
    ElapsedTime shootMiddleDelay = null;

    private boolean muteShoot = true;

    public void muteShooter() {
        muteShoot = true;
    }
    public void unmuteShooter() {
        muteShoot = false;
    }

    public void update() {
        if (muteShoot) {
            init.getShooterLeft().setVelocity(0);
            init.getShooterRight().setVelocity(0);
        } else {
            init.getShooterLeft().setVelocity(Constant.shooterMin);
            init.getShooterRight().setVelocity(Constant.shooterMin);
        }


        leftColor = UsefullFunctions.getColor(init.getColorLeft());
        middleColor = UsefullFunctions.getColor(init.getColorMiddle());
        rightColor = UsefullFunctions.getColor(init.getColorRight());

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

        switch (leftColor) {
            case ("nothing"): {
                init.getLeftLight().setPosition(0);
                break;
            }
            case ("green"): {
                init.getLeftLight().setPosition(Constant.greenLed);
                break;
            }
            case ("purple"): {
                init.getLeftLight().setPosition(Constant.purpleLed);
                break;
            }
        }

        switch (middleColor) {
            case ("nothing"): {
                init.getMiddleLight().setPosition(0);
                break;
            }
            case ("green"): {
                init.getMiddleLight().setPosition(Constant.greenLed);
                break;
            }
            case ("purple"): {
                init.getMiddleLight().setPosition(Constant.purpleLed);
                break;
            }
        }

        switch (rightColor) {
            case ("nothing"): {
                init.getRightLight().setPosition(0);
                break;
            }
            case ("green"): {
                init.getRightLight().setPosition(Constant.greenLed);
                break;
            }
            case ("purple"): {
                init.getRightLight().setPosition(Constant.purpleLed);
                break;
            }
        }


        init.outakeTrayLeft.setPosition(liftLeftPos);
        init.outakeTrayMiddle.setPosition(liftMiddlePos);
        init.outakeTrayRight.setPosition(liftRightPos);



        init.getHoodLeftServo().setPosition(Constant.hoodDown);
        init.getHoodRightServo().setPosition(Constant.hoodDown);

        telemetry.addData("shooter velocity", init.getShooterLeft().getVelocity());
//        telemetry.addData("left color", leftColor);
//        telemetry.addData("middle color", middleColor);
//        telemetry.addData("right color", rightColor);
//
//        telemetry.addData("left green", init.getColorLeft().green());
//        telemetry.addData("middle green", init.getColorMiddle().green());
//        telemetry.addData("right green", init.getColorRight().green());
//        telemetry.addData("left red", init.getColorLeft().red());
//        telemetry.addData("left blue", init.getColorLeft().blue());
//        telemetry.addData("left raw", init.getColorLeft().rawOptical());
//        telemetry.addData("left argb", init.getColorLeft().argb());
//        telemetry.addData("left distance", init.getColorLeft().getDistance(DistanceUnit.MM));
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

    public void shootRight(){
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