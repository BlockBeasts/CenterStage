package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    public Intake(Init init) {
        this.init= init;
    }

    Init init;


    Telemetry telemetry;

    DcMotor intakemoter;





    public void intakeOn() {
        init.getIntakemoter().setPower(1);
    }

    public void intakeOff() {
        init.getIntakemoter().setPower(0);
    }

    public void intakeReverse() {
        init.getIntakemoter().setPower(-1);
    }



}