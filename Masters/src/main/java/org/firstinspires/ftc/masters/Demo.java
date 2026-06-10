package org.firstinspires.ftc.masters;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp (name="demo java")
public class Demo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        //DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        boolean motorOn = false;


        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.aWasPressed()) {
                if (motorOn==false) {
                    frontLeft.setPower(0.5);
                    backLeft.setPower(0.5);
                    motorOn= true;
                } else{
                    frontLeft.setPower(0);
                    backLeft.setPower(0);
                    motorOn= false;
                }
            }

            if (gamepad1.left_stick_y>0.1){

            }

        }

    }
}
