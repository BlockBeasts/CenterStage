package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outake implements Component{

    Init init;

    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor shoota;
    DcMotor shootb;

    double launchAngle = 0.0;
    double fireAngle = 25.0;

    public void initializeHardware() {

    }

    public Outake(Init init, Telemetry telemetry){

        this.init = init;
        this.telemetry=telemetry;
        this.shoota=init.getShootAmoter();
        this.shootb=init.getShootAmoter();
    }


    public void outakeon() {

        shoota.setTargetPosition(usefullMath.angleToTicks(fireAngle, 537.7));
        shootb.setTargetPosition(usefullMath.angleToTicks(fireAngle, 537.7));

    }



}