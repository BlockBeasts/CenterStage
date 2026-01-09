package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    Init init;


    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor lift;

    public Lift(Init init){
        this.init = init;
    }




    public void liftBot() {

        init.getLiftMotor().setTargetPosition(ITDCons.upPos);
        init.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getLiftMotor().setPower(1);

    }
    public void lowerBot() {
        init.getLiftMotor().setTargetPosition(ITDCons.downPos);

        init.getLiftMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getLiftMotor().setPower(1);

    }



}