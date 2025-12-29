package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;


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

        init.getShootAmoter().setPower(1.0);
        init.getShootBmoter().setPower(1.0);
    }
    public void outakeOff() {

        init.getShootAmoter().setPower(0.0);
        init.getShootBmoter().setPower(0.0);
    }

    private boolean isInLaunchPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
    }
    private boolean isInResetPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
    }

    public void launch() {
        mode = "launch";
    }

    public void update() {
        if (Objects.equals(mode, "launch")) {
            if (init.getShootAmoter().getCurrentPosition() == UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)) {
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