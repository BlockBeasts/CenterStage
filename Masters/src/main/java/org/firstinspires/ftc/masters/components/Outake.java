package org.firstinspires.ftc.masters.components;

import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outake {

    Init init;
    String mode = "off";
    private final PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public double target = 0;
    public final double ticks_in_degree = 537.7 / 360;

    public Outake(Init init) {
        this.init= init;
        controller = new PIDController (p, i, d);
    }

    public void outakeUp() {

        init.getShootMotorA().setPower(1);
        init.getShootMotorB().setPower(1);
    }
//    public void outakeDown() {
//
//        init.getShootMotorA().setPower(-1);
//        init.getShootMotorB().setPower(-1);
//    }
    public void outakeOff() {

        init.getShootMotorA().setPower(0.0);
        init.getShootMotorB().setPower(0.0);
    }
//    public void outakeHold() {
//        init.getShootMotorA().setPower(-1);
//        init.getShootMotorB().setPower(-1);
//    }

    public boolean isInDownPos() {
        return (init.getShootMotorA().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.downAngle - 5, ITDCons.shootPPR)
                && init.getShootMotorB().getCurrentPosition() >= UsefullMath.angleToTicks(ITDCons.downAngle - 5, ITDCons.shootPPR));
    }
    public boolean isInUpPos() {
        return (init.getShootMotorA().getCurrentPosition() <= UsefullMath.angleToTicks(ITDCons.launchAngle + 5, ITDCons.shootPPR)
                && init.getShootMotorB().getCurrentPosition() <= UsefullMath.angleToTicks(ITDCons.launchAngle + 5, ITDCons.shootPPR));
    }

    public void launch() {

        mode = "launch";
        target = 0;
    }
    public void down() {

        mode = "down";
        target = ITDCons.downAngle;
    }



    public void updatePID() {
        controller.setPID(p, i, d);
        int slidePos = init.getShootMotorA().getCurrentPosition();
        double pid = controller.calculate(slidePos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        init.getShootMotorA().setPower(power);
        init.getShootMotorB().setPower(power);
    }


    public void  update(Telemetry telemetry) {

        telemetry.update();
        telemetry.addData(" motA pos: ", UsefullMath.ticksToAngle(init.getShootMotorA().getCurrentPosition(), ITDCons.shootPPR));
        telemetry.addData(" motB pos: ", UsefullMath.ticksToAngle(init.getShootMotorB().getCurrentPosition(), ITDCons.shootPPR));



        if (target == 0 && isInUpPos() ){
            outakeOff();
        } else if (target ==0){
            outakeUp();
        } else {
            updatePID();
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