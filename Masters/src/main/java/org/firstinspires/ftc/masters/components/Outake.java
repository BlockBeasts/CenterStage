package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Outake {

    Init init;
    String mode = "off";
    private final PIDController controller;

    public static double p = 0.04, i = 0, d = 0;
    public static double f = 0.1;

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


        return (init.getShootMotorA().getCurrentPosition() >= ITDCons.downAngle-5
                && init.getShootMotorB().getCurrentPosition() >= ITDCons.downAngle - 5);

    }
    public boolean isInUpPos() {
        return (init.getShootMotorA().getCurrentPosition() <=ITDCons.launchAngle + 5
                && init.getShootMotorB().getCurrentPosition() <= ITDCons.launchAngle + 5);
    }

    public void launch() {

        mode = "launch";
        target = 0;
    }
    public void down() {

        mode = "down";
        target = ITDCons.downAngle;
    }



    public void updatePID(Telemetry telemetry) {
        controller.setPID(p, i, d);
        int slidePos = init.getShootMotorA().getCurrentPosition();
        double pid = controller.calculate(slidePos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        init.getShootMotorA().setPower(power);
        init.getShootMotorB().setPower(power);

        telemetry.addData ("power", power);
    }


    public void  update(Telemetry telemetry) {


        telemetry.addData(" motA pos: ", init.getShootMotorA().getCurrentPosition());
        telemetry.addData(" motB pos: ", init.getShootMotorB().getCurrentPosition());
        telemetry.addData("target", target );
        telemetry.addData("mode", mode);
        telemetry.update();


        if (target == 0 && isInUpPos() ){
            telemetry.addData ("power", 0);
            outakeOff();
//        } else if (target ==0){
//            telemetry.addData ("power", 1);
//            outakeUp();
        } else {
            updatePID(telemetry);
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