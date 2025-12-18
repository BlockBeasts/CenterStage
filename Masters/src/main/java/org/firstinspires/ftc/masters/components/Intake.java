package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.masters.components.Init;
public class Intake implements Component{

    Init init;
    Telemetry telemetry;

    DcMotor intakemoter;

    public void initializeHardware() {

    }

    public Intake(Init init, Telemetry telemetry){

        this.init = init;
        this.telemetry=telemetry;
        this.intakemoter=init.getIntakemoter();

    }

    public void intakeOn() {
        intakemoter.setPower(1);
    }

    public void intakeReverse() {
        intakemoter.setPower(-1);
    }



}