package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    Init2 init2;


    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor lift;

    public Lift(Init2 init2){
        this.init2 = init2;
    }




    public void liftBot() {

        init2.getLiftMotor().setTargetPosition(ITDCons.upPos);
        init2.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init2.getLiftMotor().setPower(1);

    }
    public void lowerBot() {
        init2.getLiftMotor().setTargetPosition(ITDCons.downPos);

        init2.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init2.getLiftMotor().setPower(1);

    }



}