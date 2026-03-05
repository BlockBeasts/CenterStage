package org.firstinspires.ftc.masters.components;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.apache.commons.math3.genetics.FixedElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    Init init;
    Outake outake;
    Telemetry telemetry;

    ElapsedTime elapsedTime = null;
    boolean intakeOn = false;
    boolean intakeReverse = false;


    public Intake(Init init, Outake outake, Telemetry telemetry) {

        this.init= init;
        this.outake = outake;
        this.telemetry = telemetry;
    }

    public void intakeOn() {
        init.getIntakeMotor().setPower(1);
        intakeOn = true;
        intakeReverse = false;
    }

    public void intakeOff() {
        init.getIntakeMotor().setPower(0);
        intakeOn = false;
        intakeReverse = false;
    }

    public void intakeReverse() {
        init.getIntakeMotor().setPower(-0.5);
        intakeOn = false;
        intakeReverse = true;

    }

    public void reverseForBalls(){
        intakeReverse = true;
        intakeOn = false;
        elapsedTime = new ElapsedTime();
    }

    public void update() {

        if (intakeReverse){
            init.getIntakeMotor().setPower(-0.5);
            if (elapsedTime!=null && elapsedTime.milliseconds>1000){
                elapsedTime =null;
                init.getIntakeMotor().setPower(1);
            }
        } else if (intakeOn){
            init.getIntakeMotor().setPower(1);
        } else {
            intakeOff();
        }

        if (outake.has3Balls() && intakeOn && elapsedTime ==null){
            intakeReverse();
            elapsedTime = new ElapsedTime();
        }

        if (outake.has3Balls() && elapsedTime!=null &&     elapsedTime.milliseconds()>1000){
            intakeOff();
            elapsedTime = null;
        }
    }

//    public boolean upToRumble() {
//        return init.getIntakeMotor().getPower() == -.5;
//    }


}