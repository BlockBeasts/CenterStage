package org.firstinspires.ftc.masters.components;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    Init init;
    Telemetry telemetry;


    public Intake(Init init) {
        this.init= init;
    }

    public void intakeOn() {
        init.getIntakeMotor().setPower(1);
    }

    public void intakeOff() {
        init.getIntakeMotor().setPower(0);
    }

    public void intakeReverse() {
        init.getIntakeMotor().setPower(-0.5);
    }

    //TODO:
    //should voltage check be here?


}