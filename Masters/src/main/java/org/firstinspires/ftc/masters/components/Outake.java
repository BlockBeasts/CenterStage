package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

public class Outake {

    Init init;

    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor shoota;
    DcMotor shootb;

    String mode = "off";






    private void outakeOn() {
        init.getShootAmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
        init.getShootBmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
        init.getShootAmoter().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getShootBmoter().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getShootAmoter().setPower(1.0);
        init.getShootBmoter().setPower(1.0);
    }
    private void outakeOff() {
        init.getShootAmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
        init.getShootBmoter().setTargetPosition(usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
        init.getShootAmoter().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getShootBmoter().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        init.getShootAmoter().setPower(1.0);
        init.getShootBmoter().setPower(1.0);
    }

    private boolean isInLaunchPos() {
        return (init.getShootAmoter().getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
    }
    private boolean isInResetPos() {
        return (init.getShootAmoter().getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= usefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
    }

    public void launch() {
        mode = "launch";
    }

    public void update() {

        if (Objects.equals(mode, "launch")) {
            outakeOn();
            if (!init.getShootAmoter().isBusy()) {
                init.getShootAmoter().setPower(0.0);
            }
            if (!init.getShootBmoter().isBusy()) {
                init.getShootBmoter().setPower(0.0);
            }
            if (isInLaunchPos()) {
                outakeOff();
                mode = "reset";
            }
        }
        if (Objects.equals(mode, "reset")) {
            if (!init.getShootAmoter().isBusy()) {
                init.getShootAmoter().setPower(0.0);
            }
            if (!init.getShootBmoter().isBusy()) {
                init.getShootBmoter().setPower(0.0);
            }
            if (isInResetPos()) {
                mode = "off";
            }
        }
    }
}