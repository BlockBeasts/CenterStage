package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

public class Outake {
    public Outake(Init init) {
     this.init= init;
    }
    Init init;
    Telemetry telemetry;

    DcMotor shoota;
    DcMotor shootb;

    String mode = "off";


//    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(800, 0, 5, 12);
//
//    private VoltageSensor batteryVoltageSensor;

    public void outakeOn() {

        init.getShootAmoter().setPower(1);
        init.getShootBmoter().setPower(1);
    }
    public void outakerev() {

        init.getShootAmoter().setPower(-1);
        init.getShootBmoter().setPower(-1);
    }
    public void outakeOff() {

        init.getShootAmoter().setPower(0.0);
        init.getShootBmoter().setPower(0.0);
    }
    public void outakeHold() {
        init.getShootAmoter().setPower(0.1);
        init.getShootBmoter().setPower(0.1);
    }

    public boolean isInLaunchPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
    }
    public boolean isInResetPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
    }

    public void launch() {
        outakerev();
        mode = "launch";
    }
    public void reset() {
        outakeOn();
        mode = "reset";
    }


    public void update(Telemetry telemetry) {

        telemetry.update();
        telemetry.addData(" motA pos: ", UsefullMath.ticksToAngle(init.getShootAmoter().getCurrentPosition(), ITDCons.shootPPR));
        telemetry.addData(" motB pos: ", UsefullMath.ticksToAngle(init.getShootBmoter().getCurrentPosition(), ITDCons.shootPPR));

        if (Objects.equals(mode, "launch")) {
            if (init.getShootAmoter().getCurrentPosition() <= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)) {
                outakeOff();
                mode = "off";
            }
        }
        if (Objects.equals(mode, "reset")) {
            if (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)) {
                outakeOff();
                mode = "off";
            }
        }
//        if (Objects.equals(mode, "launch")) {
//            outakeOn();
//            if (!init.getShootAmoter().isBusy()) {
//                init.getShootAmoter().setPower(0.0);
//            }
//            if (!init.getShootBmoter().isBusy()) {
//                init.getShootBmoter().setPower(0.0);
//            }
//            if (isInLaunchPos()) {
//                outakeOff();
//                mode = "reset";
//            }

//        if (Objects.equals(mode, "reset")) {
//            if (!init.getShootAmoter().isBusy()) {
//                init.getShootAmoter().setPower(0.0);
//            }
//            if (!init.getShootBmoter().isBusy()) {
//                init.getShootBmoter().setPower(0.0);
//            }
//            if (isInResetPos()) {
//                mode = "off";
//            }
//        }
    }

//    private void setPIDFCoefficients(DcMotorEx motor, PIDFCoefficients coefficients) {
//        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(
//                coefficients.p, coefficients.i, coefficients.d, coefficients.f * 12 / batteryVoltageSensor.getVoltage()
//        ));
//    }
}