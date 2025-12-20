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
        shoota.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shootb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoota.setPower(1.0);
        shootb.setPower(1.0);
    }
    private void outakeOff() {
        shoota.setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
        shootb.setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
        shoota.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shootb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoota.setPower(1.0);
        shootb.setPower(1.0);
    }

    private boolean isInLaunchPos() {
        return (shoota.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                && shootb.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
    }
    private boolean isInResetPos() {
        return (shoota.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                && shootb.getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
    }

    public void launch() {
        mode = "launch";
    }

    public void update() {

        if (Objects.equals(mode, "launch")) {
            outakeOn();
            if (!shoota.isBusy()) {
                shoota.setPower(0.0);
            }
            if (!shootb.isBusy()) {
                shootb.setPower(0.0);
            }
            if (isInLaunchPos()) {
                outakeOff();
                mode = "reset";
            }
        }
        if (Objects.equals(mode, "reset")) {
            if (!shoota.isBusy()) {
                shoota.setPower(0.0);
            }
            if (!shootb.isBusy()) {
                shootb.setPower(0.0);
            }
            if (isInResetPos()) {
                mode = "off";
            }
        }
    }
}