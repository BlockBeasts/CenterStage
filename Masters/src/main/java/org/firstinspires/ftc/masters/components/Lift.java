package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    Init init;


    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor lift;











    public void liftBot() {

        init.getLiftmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.upPos, 537.7));

    }
    public void lowerBot() {
        init.getLiftmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.downPos, 537.7));

    }



}