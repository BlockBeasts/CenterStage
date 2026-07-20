package org.firstinspires.ftc.masters.components;


import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Outake {
    Init init;
    Intake intake;

    Telemetry telemetry;
    int shooterVelocity = 0;

    Constant.AllianceColor allianceColor= null;
    Follower follower;
    public Outake(Init init, Telemetry telemetry, Constant.AllianceColor allianceColor) {
        this.init= init;
        this.telemetry = telemetry;
        this.allianceColor = allianceColor;
    }

    public void setIntake(Intake intake){
        this.intake = intake;
    }

    public void setFollower(Follower follower){
        this.follower = follower;
    }

    public static int motorVel = 1000;

    public static double hoodServoPos = 0;

    boolean calledIntake = false;



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
        init.getShooterLeft().setPower(0);
        init.getShooterRight().setPower(0);
        muteShoot = true;
    }
    public void startShooter() {
        muteShoot = false;
    }
    protected void colorLeft() {
        leftColor = UsefullFunctions.getColor(init.getColorLeft(), "left");
    }

    protected void colorMiddle() {
        middleColor = UsefullFunctions.getColor(init.getColorMiddle(), "middle");
    }

    protected void colorRight() {
        rightColor = UsefullFunctions.getColor(init.getColorRight(), "right");
    }

    protected void delayLeft() {
        if (shootLeftDelay!=null && shootLeftDelay.milliseconds()>delay){
            liftLeftPos = Constant.leftTrayBottom;
            shootLeftDelay = null;
        }
    }

    protected void delayMiddle() {
        if (shootMiddleDelay!=null && shootMiddleDelay.milliseconds()>delay){
            liftMiddlePos = Constant.middleTrayBottom;
            shootMiddleDelay = null;
        }
    }

    protected void delayRight() {
        if (shootRightDelay!=null && shootRightDelay.milliseconds()>delay){
            liftRightPos = Constant.rightTrayBottom;
            shootRightDelay = null;
        }
    }

    public void updateShooter() {
        if (muteShoot == false) {
            double leftVel = init.getShooterLeft().getVelocity();

            if (leftVel < shooterVelocity) {
                init.getShooterLeft().setPower(1);
                init.getShooterRight().setPower(1);
            } else {
                init.getShooterLeft().setPower(0);
                init.getShooterRight().setPower(0);
            }

        }

    }


    public void updateConstant(){
        init.getShooterLeft().setVelocity (Constant.shooterMin);
        init.getShooterRight().setVelocity(Constant.shooterMin);




        init.getHoodLeftServo().setPosition(Constant.hoodDown);
        init.getHoodRightServo().setPosition(Constant.hoodDown);


//        if (!has3Balls() ||  "nothing".equals(leftColor) || "unknown".equals(leftColor) ) {
            colorLeft();
//        }
//        if (!has3Balls() ||  "nothing".equals(middleColor) || "unknown".equals(middleColor) ) {
            colorMiddle();
//        }
//
//        if (!has3Balls() ||  "nothing".equals(rightColor) || "unknown".equals(rightColor) ) {
            colorRight();
      //  }

        delayLeft();

        delayMiddle();

        delayRight();

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


        telemetry.addData("shooter velocity Left", init.getShooterLeft().getVelocity());
        telemetry.addData("shooter velocity Right", init.getShooterRight().getVelocity());
    }

    public void update(int offsetLeft, int offsetRight) {
        updateShooter();
        if (muteShoot) {
            shooterVelocity =0;
//            init.getShooterLeft().setVelocity(0);
//            init.getShooterRight().setVelocity(0);
        } else {
            if (allianceColor == Constant.AllianceColor.BLUE) {
                if(follower.getPose().getY() > 90){
                    shooterVelocity = UsefullMath.getVelocityBlue(follower.getPose());
//                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()));
//                    init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()));
                }else {
                    shooterVelocity = UsefullMath.getVelocityBlue(follower.getPose()) + offsetLeft;
                }
            }
            if (allianceColor == Constant.AllianceColor.RED){
                if(follower.getPose().getY() > 90){
                    shooterVelocity = UsefullMath.getVelocityRed(follower.getPose());

                }else {

                    shooterVelocity = UsefullMath.getVelocityRed(follower.getPose())+ offsetLeft;
                }
            }

            if (follower.getPose().getY()<50){

                init.getHoodLeftServo().setPosition(Constant.hoodFar);
                init.getHoodRightServo().setPosition(Constant.hoodFar);
            } else {

                init.getHoodLeftServo().setPosition(Constant.hoodDown);
                init.getHoodRightServo().setPosition(Constant.hoodDown);
            }

//            init.getShooterLeft().setVelocity(Constant.shooterMin);
//            init.getShooterRight().setVelocity(Constant.shooterMin);

        }

        if (has3Balls() && !calledIntake){
            if (intake!=null){
                intake.reverseForBalls();
                calledIntake = true;
            }
        }


//        if (!has3Balls() ||  "nothing".equals(leftColor) || "unknown".equals(leftColor) ) {
            colorLeft();
//        }
//        if (!has3Balls() ||  "nothing".equals(middleColor) || "unknown".equals(middleColor) ) {
            colorMiddle();
//        }
//
//        if (!has3Balls() ||  "nothing".equals(rightColor) || "unknown".equals(rightColor) ) {
            colorRight();
       // }

        delayLeft();

        delayMiddle();

        delayRight();

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


        telemetry.addData("shooter velocity", init.getShooterLeft().getVelocity());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
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

//    public void updateAuto() {
//        if (muteShoot) {
//            init.getShooterLeft().setVelocity(0);
//            init.getShooterRight().setVelocity(0);
//        } else {
//            if (allianceColor == Constant.AllianceColor.BLUE) {
//                if(follower.getPose().getY() > 90){
//                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) );
//                    init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(follower.getPose())  );
//                }else {
//                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) - 50);
//                    init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) - 50);
//                }
//            }
//            if (allianceColor == Constant.AllianceColor.RED){
//                if(follower.getPose().getY() > 90){
//                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityRed(follower.getPose()) );
//                    init.getShooterRight().setVelocity(UsefullMath.getVelocityRed(follower.getPose()));
//                }else {
//                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityRed(follower.getPose()) - 50);
//                    init.getShooterRight().setVelocity(UsefullMath.getVelocityRed(follower.getPose()) - 50);
//                }
//            }
//
//            if (follower.getPose().getY()<50){
//
//                init.getHoodLeftServo().setPosition(Constant.hoodFar);
//                init.getHoodRightServo().setPosition(Constant.hoodFar);
//            } else {
//
//                init.getHoodLeftServo().setPosition(Constant.hoodDown);
//                init.getHoodRightServo().setPosition(Constant.hoodDown);
//            }
//
////            init.getShooterLeft().setVelocity(Constant.shooterMin);
////            init.getShooterRight().setVelocity(Constant.shooterMin);
//
//        }
//
//        if (has3Balls() && !calledIntake){
//            if (intake!=null){
//                intake.reverseForBalls();
//                calledIntake = true;
//            }
//        }
//
//
//        if (!has3Balls() ||  "nothing".equals(leftColor) || "unknown".equals(leftColor) ) {
//            colorLeft();
//        }
//        if (!has3Balls() ||  "nothing".equals(middleColor) || "unknown".equals(middleColor) ) {
//            colorMiddle();
//        }
//
//        if (!has3Balls() ||  "nothing".equals(rightColor) || "unknown".equals(rightColor) ) {
//            colorRight();
//        }
//
//        delayLeft();
//
//        delayMiddle();
//
//        delayRight();
//
//        switch (leftColor) {
//            case ("nothing"): {
//                init.getLeftLight().setPosition(0);
//                break;
//            }
//            case ("green"): {
//                init.getLeftLight().setPosition(Constant.greenLed);
//                break;
//            }
//            case ("purple"): {
//                init.getLeftLight().setPosition(Constant.purpleLed);
//                break;
//            }
//            case ("unknown"):{
//                init.getLeftLight().setPosition(Constant.orangeLed);
//                break;
//            }
//        }
//
//        switch (middleColor) {
//            case ("nothing"): {
//                init.getMiddleLight().setPosition(0);
//                break;
//            }
//            case ("green"): {
//                init.getMiddleLight().setPosition(Constant.greenLed);
//                break;
//            }
//            case ("purple"): {
//                init.getMiddleLight().setPosition(Constant.purpleLed);
//                break;
//            }
//            case ("unknown"):{
//                init.getMiddleLight().setPosition(Constant.orangeLed);
//                break;
//            }
//        }
//
//        switch (rightColor) {
//            case ("nothing"): {
//                init.getRightLight().setPosition(0);
//                break;
//            }
//            case ("green"): {
//                init.getRightLight().setPosition(Constant.greenLed);
//                break;
//            }
//            case ("purple"): {
//                init.getRightLight().setPosition(Constant.purpleLed);
//                break;
//            }
//            case ("unknown"):{
//                init.getRightLight().setPosition(Constant.orangeLed);
//                break;
//            }
//        }
//
//
//        init.outakeTrayLeft.setPosition(liftLeftPos);
//        init.outakeTrayMiddle.setPosition(liftMiddlePos);
//        init.outakeTrayRight.setPosition(liftRightPos);
//
//
//        telemetry.addData("shooter velocity", init.getShooterLeft().getVelocity());
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
////        telemetry.addData("left color", leftColor);
////        telemetry.addData("middle color", middleColor);
////        telemetry.addData("right color", rightColor);
////
////        telemetry.addData("left green", init.getColorLeft().green());
////        telemetry.addData("middle green", init.getColorMiddle().green());
////        telemetry.addData("right green", init.getColorRight().green());
////        telemetry.addData("left red", init.getColorLeft().red());
////        telemetry.addData("left blue", init.getColorLeft().blue());
////        telemetry.addData("left raw", init.getColorLeft().rawOptical());
////        telemetry.addData("left argb", init.getColorLeft().argb());
////        telemetry.addData("left distance", init.getColorLeft().getDistance(DistanceUnit.MM));
//    }

    public void updateAutoMotifs() {
        if (muteShoot) {
            init.getShooterLeft().setVelocity(0);
            init.getShooterRight().setVelocity(0);
        } else {
            if (allianceColor == Constant.AllianceColor.BLUE) {
                if(follower.getPose().getY() > 90){
                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) -50 );
                    init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) -50 );
                }else {
                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) - 80);
                    init.getShooterRight().setVelocity(UsefullMath.getVelocityBlue(follower.getPose()) - 80);
                }
            }
            if (allianceColor == Constant.AllianceColor.RED){
                if(follower.getPose().getY() > 90){
                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityRed(follower.getPose())-30 );
                    init.getShooterRight().setVelocity(UsefullMath.getVelocityRed(follower.getPose())-30);
                }else {
                    init.getShooterLeft().setVelocity(UsefullMath.getVelocityRed(follower.getPose()) - 80);
                    init.getShooterRight().setVelocity(UsefullMath.getVelocityRed(follower.getPose()) - 80);
                }
            }

            if (follower.getPose().getY()<50){

                init.getHoodLeftServo().setPosition(Constant.hoodFar);
                init.getHoodRightServo().setPosition(Constant.hoodFar);
            } else {

                init.getHoodLeftServo().setPosition(Constant.hoodDown);
                init.getHoodRightServo().setPosition(Constant.hoodDown);
            }

//            init.getShooterLeft().setVelocity(Constant.shooterMin);
//            init.getShooterRight().setVelocity(Constant.shooterMin);

        }

        if (has3Balls() && !calledIntake){
            if (intake!=null){
                intake.reverseForBalls();
                calledIntake = true;
            }
        }


        if (!has3Balls() ||  "nothing".equals(leftColor) || "unknown".equals(leftColor) ) {
            colorLeft();
        }
        if (!has3Balls() ||  "nothing".equals(middleColor) || "unknown".equals(middleColor) ) {
            middleColor = UsefullFunctions.getColor(init.getColorMiddle(), "middle");
        }

        if (!has3Balls() ||  "nothing".equals(rightColor) || "unknown".equals(rightColor) ) {
            rightColor = UsefullFunctions.getColor(init.getColorRight(), "right");
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


        telemetry.addData("shooter velocity", init.getShooterLeft().getVelocity());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
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
            calledIntake = false;
        }
    }

    public void shootMiddle(){
        if (shootMiddleDelay == null) {
            liftMiddlePos = Constant.middleTrayTop;
            shootMiddleDelay = new ElapsedTime();
            resetMiddleColor();
            calledIntake = false;
        }
    }

    public void shootRight(){
        if (shootRightDelay == null) {
            liftRightPos = Constant.rightTrayTop;
            shootRightDelay = new ElapsedTime();
            resetRightColor();
            calledIntake = false;
        }
    }

    public void shootAll(){
        shootLeft();
        shootMiddle();
        shootRight();
        calledIntake = false;
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


    public boolean upToSpeed() {
        return (UsefullMath.getVelocityBlue(follower.getPose()) - 20) <= init.getShooterRight().getVelocity();
    }


}