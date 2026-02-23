package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.masters.components.Intake;

@TeleOp(group ="Test", name="testdrivetrain")
@Disabled
public class testdive extends LinearOpMode {
                     
    double max_power = 1;




    Intake intake;


    double X_power = 0;
    double y_power = 0;

    double m1_power = 0;
    double m2_power = 0;
    double m3_power = 0;
    double m4_power = 0;
    double outSped = 0;

    double turn_power = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRight = hardwareMap.dcMotor.get("backRight");

  
        DcMotor outakem1 = hardwareMap.dcMotor.get("outakem1");
        DcMotor outakem2 = hardwareMap.dcMotor.get("outakem2");
        DcMotor selector = hardwareMap.dcMotor.get("selector");

        

        waitForStart();


        while(opModeIsActive()){



            X_power = (gamepad1.left_stick_y * max_power)*-1;
            y_power = (gamepad1.left_stick_x * max_power);
            turn_power = ((gamepad1.left_trigger + (gamepad1.right_trigger * -1)) * max_power);


            m1_power = X_power + y_power - turn_power;
            m2_power = X_power - y_power + turn_power;
            m3_power = X_power - y_power - turn_power;
            m4_power = X_power + y_power + turn_power;

            telemetry.addData("m1: ", m1_power);
            telemetry.addData("m2: ", m2_power);
            telemetry.addData("m3: ", m3_power);
            telemetry.addData("m4: ", m4_power);



            frontLeft.setPower(m1_power);
            backLeft.setPower(m2_power);

            frontRight.setPower(m3_power);
            backRight.setPower(m4_power);

            telemetry.update();



        }
    }
}
