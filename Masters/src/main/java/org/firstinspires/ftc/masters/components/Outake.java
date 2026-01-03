package org.firstinspires.ftc.masters.components;

import com.arcrobotics.ftclib.controller.PIDController;
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
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public double target = ITDCons.fireAngle;
    public final double ticks_in_degree = 537.7 / 180;

    public void outakeUp() {

        init.getShootAmoter().setPower(1);
        init.getShootBmoter().setPower(1);
    }
    public void outakeDown() {

        init.getShootAmoter().setPower(-1);
        init.getShootBmoter().setPower(-1);
    }
    public void outakeOff() {

        init.getShootAmoter().setPower(0.0);
        init.getShootBmoter().setPower(0.0);
    }
    public void outakeHold() {
        init.getShootAmoter().setPower(-1);
        init.getShootBmoter().setPower(-1);
    }

    public boolean isInResetPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.fireAngle, ITDCons.shootPPR));
    }
    public boolean isInUpPos() {
        return (init.getShootAmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR)
                && init.getShootBmoter().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.launchAngle, ITDCons.shootPPR));
    }

    public void launch() {

        mode = "launch";
    }
    public void reset() {

        mode = "down";
    }

    public void init() {
        controller = new PIDController(p, i, d);

    }

    public void updatePID() {
        controller.setPID(p, i, d);
        int slidePos = init.getShootAmoter().getCurrentPosition();
        double pid = controller.calculate(slidePos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        init.getShootAmoter().setPower(power);
        init.getShootBmoter().setPower(power);
    }


    public void  update(Telemetry telemetry) {

        telemetry.update();
        telemetry.addData(" motA pos: ", UsefullMath.ticksToAngle(init.getShootAmoter().getCurrentPosition(), ITDCons.shootPPR));
        telemetry.addData(" motB pos: ", UsefullMath.ticksToAngle(init.getShootBmoter().getCurrentPosition(), ITDCons.shootPPR));

        if (Objects.equals(mode, "launch")) {
            outakeUp();
            if (isInUpPos()) {

            }
        }
        if (Objects.equals(mode, "down")) {
            outakeDown();
            if (isInResetPos()) {
                outakeHold();
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