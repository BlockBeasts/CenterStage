package org.firstinspires.ftc.masters.components;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    Init init;
    Outake outake;
    Telemetry telemetry;

    ElapsedTime elapsedTime = null;
    boolean intakeOn = false;


    public Intake(Init init, Outake outake, Telemetry telemetry) {

        this.init= init;
        this.outake = outake;
        this.telemetry = telemetry;
    }

    public void intakeOn() {
        init.getIntakeMotor().setPower(1);
        intakeOn = true;
    }

    public void intakeOff() {
        init.getIntakeMotor().setPower(0);
        intakeOn = false;
    }

    public void intakeReverse() {
        init.getIntakeMotor().setPower(-0.5);
        intakeOn = false;
    }

    public void update() {

        if (outake.has3Balls() && elapsedTime ==null){
            intakeReverse();
            elapsedTime = new ElapsedTime();
        }

        if (outake.has3Balls() && elapsedTime.milliseconds()>2000){
            intakeOff();
            elapsedTime = null;
        }
    }


}