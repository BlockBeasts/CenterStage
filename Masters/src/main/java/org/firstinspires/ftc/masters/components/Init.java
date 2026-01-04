package org.firstinspires.ftc.masters.components;

//import com.pedropathing.localization.GoBildaPinpointDriver;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Init {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor intakeMotor;
    DcMotor shootA;
    DcMotor shootB;
    DcMotor lift;

    public Init(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        intakeMotor = hardwareMap.dcMotor.get("intakemoter");
        shootA = hardwareMap.dcMotor.get("shoota");
        shootB = hardwareMap.dcMotor.get("shootb");
        lift = hardwareMap.dcMotor.get("lift");


        shootA.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        shootA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        shootB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shootA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shootB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shootA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shootB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public DcMotor getIntakeMotor() {
        return intakeMotor;
    }

    public DcMotor getFrontLeft() {
        return frontLeft;
    }

    public DcMotor getBackLeft() {
        return backLeft;
    }

    public DcMotor getFrontRight() {
        return frontRight;
    }

    public DcMotor getBackRight() {
        return backRight;
    }

    public DcMotor getShootMotorA() {
        return shootA;
    }

    public DcMotor getShootMotorB() {
        return shootB;
    }

    public DcMotor getLiftMotor() {
        return lift;
    }
}