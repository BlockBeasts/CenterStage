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


    public void stopLift(){
        init.getLift().setPower(0);
    }

    public void liftBot() {

        init.getLift().setPower(-0.9);
    }

    public void lowerBot() {
        init.getLift().setPower(0.9);
    }



}