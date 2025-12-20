package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift implements Component{

    Init init;


    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor lift;







    public void initializeHardware() {

    }

    public Lift(Init init, Telemetry telemetry){

        this.init = init;
        this.telemetry=telemetry;
        this.lift=init.getLiftmoter();

    }


    public void liftBot() {

        lift.setTargetPosition(usefullMath.angleToTicks(ITDCons.upPos, 537.7));

    }
    public void lowerBot() {
        lift.setTargetPosition(usefullMath.angleToTicks(ITDCons.downPos, 537.7));

    }



}