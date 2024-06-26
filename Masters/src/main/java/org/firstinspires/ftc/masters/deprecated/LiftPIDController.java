package org.firstinspires.ftc.masters.deprecated;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Config
public class LiftPIDController {

    DcMotorEx mainSlideMotor, slideMotor2, slideMotor3;
    PIDController liftController;
    int target =0;
    public static double multiplier = 1;
    public static double p=0.03, i=0, d=0.00001;
    public static double f=0.03;

    public LiftPIDController (DcMotorEx mainSlideMotor, DcMotorEx slideMotor2, DcMotorEx slideMotor3){
        this.mainSlideMotor = mainSlideMotor;
        this.slideMotor2 = slideMotor2;
        this.slideMotor3 = slideMotor3;
        liftController = new PIDController(p, i,d);
    }

    public void setP(double p){
        this.p=p;
        liftController.setP(p);
    }

    public void setTarget(int target){
        this.target = target;
    }

    public double calculatePower(){

       // liftController.setPID(p, i, d);
        int liftPos = mainSlideMotor.getCurrentPosition();
        double pid = liftController.calculate(liftPos, target);

        double power = pid +f;

        if (target == 0 && liftPos<70){
            power = 0;
        } else{
           power = power*multiplier;
       }

        return power;
    }

    public double calculatePower(DcMotorEx motor){

        int liftPos = motor.getCurrentPosition();



        if (target == 0 && liftPos<70){
            return 0;
        } else{
            double pid = liftController.calculate(liftPos, target);
            return (pid +f)*multiplier;
        }
    }

    public double calculatePower(int liftPos){

        if (target == 0 && liftPos<70){
            return 0;
        } else{
            double pid = liftController.calculate(liftPos, target);
            return (pid +f)*multiplier;
        }
    }

    public double calculatePowerSingle(int liftPos){

        if (target == 0 && liftPos<70){
            return 0;
        } else{
            double pid = liftController.calculate(liftPos, target);
            return (pid +f)*multiplier;
        }
    }




}
