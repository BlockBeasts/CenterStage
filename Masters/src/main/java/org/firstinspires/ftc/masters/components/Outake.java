package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outake implements Component{

    Init init;
    Telemetry telemetry;

    DcMotor shoota;
    DcMotor shootb;

    public void initializeHardware() {

    }

    public Outake(Init init, Telemetry telemetry){

        this.init = init;
        this.telemetry=telemetry;
        this.shoota=init.getShootAmoter();
        this.shootb=init.getShootAmoter();
    }


    public void outakeon() {


    }



}