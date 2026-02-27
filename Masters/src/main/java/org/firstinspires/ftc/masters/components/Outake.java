package org.firstinspires.ftc.masters.components;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Outake {
    Init init;

    Telemetry telemetry;

    Constant.AllianceColor allianceColor= null;
    public Outake(Init init, Telemetry telemetry, Constant.AllianceColor allianceColor) {
        this.init= init;
        this.telemetry = telemetry;
        this.allianceColor = allianceColor;
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

    public void stopShooter() {
        muteShoot = true;
    }
    public void startShooter() {
        muteShoot = false;
    }

    public void update() {
        if (muteShoot) {
            init.getShooterLeft().setVelocity(0);
            init.getShooterRight().setVelocity(0);
        } else {
            if (allianceColor == Constant.AllianceColor.BLUE) {
                init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(init.getPinpoint().getPosX(DistanceUnit.INCH), init.getPinpoint().getPosY(DistanceUnit.INCH)));
                init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(init.getPinpoint().getPosX(DistanceUnit.INCH), init.getPinpoint().getPosY(DistanceUnit.INCH)));
            }
            if (allianceColor == Constant.AllianceColor.RED){
                init.getShooterLeft().setVelocity(UsefullMath.getVelocityRed(init.getPinpoint().getPosX(DistanceUnit.INCH), init.getPinpoint().getPosY(DistanceUnit.INCH)));
                init.getShooterRight().setVelocity(UsefullMath.getVelocityRed(init.getPinpoint().getPosX(DistanceUnit.INCH), init.getPinpoint().getPosY(DistanceUnit.INCH)));
            }

        }


        if (!has3Balls() ||  "nothing".equals(leftColor) || "unknown".equals(leftColor) ) {
            leftColor = UsefullFunctions.getColor(init.getColorLeft());
        }
        if (!has3Balls() ||  "nothing".equals(middleColor) || "unknown".equals(middleColor) ) {
            middleColor = UsefullFunctions.getColor(init.getColorMiddle());
        }

        if (!has3Balls() ||  "nothing".equals(rightColor) || "unknown".equals(rightColor) ) {
            rightColor = UsefullFunctions.getColor(init.getColorRight());
        }

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
            case ("unknown"):{
                init.getLeftLight().setPosition(Constant.orangeLed);
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
            case ("unknown"):{
                init.getMiddleLight().setPosition(Constant.orangeLed);
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
            case ("unknown"):{
                init.getRightLight().setPosition(Constant.orangeLed);
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
        if ("green".equals(leftColor)){
            shootLeft();
        } else if ("green".equals(middleColor)){
            shootMiddle();
        } else if ("green".equals(rightColor)){
            shootRight();
        }
    }

    public void shootPurple() {
        if ("purple".equals(leftColor)){
            shootLeft();
        } else if ("purple".equals(middleColor)){
            shootMiddle();
        } else if ("purple".equals(rightColor)){
            shootRight();
        }
    }



    public void shootLeft(){
        if (shootLeftDelay == null) {
            liftLeftPos = Constant.leftTrayTop;
            shootLeftDelay = new ElapsedTime();
            resetLeftColor();
        }
    }

    public void shootMiddle(){
        if (shootMiddleDelay == null) {
            liftMiddlePos = Constant.middleTrayTop;
            shootMiddleDelay = new ElapsedTime();
            resetMiddleColor();
        }
    }

    public void shootRight(){
        if (shootRightDelay == null) {
            liftRightPos = Constant.rightTrayTop;
            shootRightDelay = new ElapsedTime();
            resetRightColor();
        }
    }

    public void shootAll(){
        shootLeft();
        shootMiddle();
        shootRight();
    }
//    public void reset() {
//        liftLeftPos = Constant.leftTrayBottom;
//        liftMiddlePos = Constant.middleTrayBottom;
//        liftRightPos = Constant.rightTrayBottom;
//        shootLeftDelay = null;
//        shootRightDelay = null;
//        shootMiddleDelay = null;
//    }
//
//    public void resetColor(){
//        resetLeftColor();;
//        resetMiddleColor();
//        resetRightColor();
//    }

    public void resetLeftColor(){
        leftColor = "nothing";
    }

    public void resetRightColor(){
        rightColor = "nothing";
    }

    public void resetMiddleColor(){
        middleColor = "nothing";
    }

    public boolean has3Balls(){
        return ("unknown".equals(leftColor) || "green".equals(leftColor) || "purple".equals(leftColor) )&&
                ("unknown".equals(middleColor) || "green".equals(middleColor) || "purple".equals(middleColor)) &&
                ("unknown".equals(rightColor) || "green".equals(rightColor) || "purple".equals(rightColor) );

    }

    protected void setHoodLow(){
        init.getHoodRightServo().setPosition(Constant.hoodDown);
    }






}