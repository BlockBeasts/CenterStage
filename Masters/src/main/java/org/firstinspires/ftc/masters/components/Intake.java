package org.firstinspires.ftc.masters.components;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    public Intake(Init2 init2) {
        this.init2= init2;
    }

    Init2 init2;


    Telemetry telemetry;







    public void intakeOn() {
        init2.getIntakeMotor().setPower(1);
    }

    public void intakeOff() {
        init2.getIntakeMotor().setPower(0);
    }

    public void intakeReverse() {
        init2.getIntakeMotor().setPower(-0.5);
    }



}