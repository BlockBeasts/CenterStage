package org.firstinspires.ftc.masters.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    Init init;


    UsefullMath usefullMath;
    Telemetry telemetry;

    DcMotor lift;

    public Lift(Init init){
        this.init = init;
    }


    public void stopLift(){
        init.getLift().setPower(0);
    }

    public void lowerBot(double power){
        init.getLift().setPower(-1);
    }

    public void liftBot(double power){
        init.getLift().setPower(1);
    }

    public void lowerBot() {

        if (init.getLift().getCurrentPosition()>0) {
            init.getLift().setPower(-1);
        } else {
            init.getLift().setPower(0);
        }
    }

    public void liftRobot() {
        if (init.getLift().getCurrentPosition()<1800) {
            init.getLift().setPower(1);
        } else {
            init.getLift().setPower(0);
        }
    }

    public void update(){
        if (init.getLift().getCurrentPosition()>1800 || init.getLift().getCurrentPosition()<0){
            stopLift();
        }
    }

    public int getCurrentPosition(){
        return  init.getLift().getCurrentPosition();
    }

}