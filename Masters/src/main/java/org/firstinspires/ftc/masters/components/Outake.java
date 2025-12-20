package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

public class Outake implements Component{

    Init init;

    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor shoota;
    DcMotor shootb;

    String mode = "off";
    boolean armed = false;

    public void initializeHardware() {

    }

    public Outake(Init init, Telemetry telemetry){

        this.init = init;
        this.telemetry=telemetry;
        this.shoota=init.getShootAmoter();
        this.shootb=init.getShootAmoter();
    }


    private void outakeOn() {

        shoota.setTargetPosition(usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
        shootb.setTargetPosition(usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));

    }
    private void outakeOff() {

        shoota.setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
        shootb.setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));

    }

    public void launch() {
        mode = "launch";
    }



    public void update() {
        if (Objects.equals(mode, "launch") || Objects.equals(mode, "reset")) {


            if (!armed) {
                outakeOn();
                armed = true;
            } else {
                if (shoota.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                        && shootb.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)) {
                    outakeOff();
                    mode = "reset";


                } else if (Objects.equals(mode, "reset")) {
                    if (shoota.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                            && shootb.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)) {
                        mode = "off";
                        armed = false;
                    }
                }
            }
        }
    }



}