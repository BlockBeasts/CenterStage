package org.firstinspires.ftc.masters.components;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Init {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor intakemoter;
    DcMotor shoota;
    DcMotor shootb;

    DcMotor lift;
    public Init(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        intakemoter = hardwareMap.dcMotor.get("intakemoter");
        shoota = hardwareMap.dcMotor.get("shoota");
        shootb = hardwareMap.dcMotor.get("shootb");
        lift = hardwareMap.dcMotor.get("lift");



        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakemoter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shootb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public DcMotor getIntakemoter() {
        return intakemoter;
    }
    public DcMotor getShootAmoter() {
        return shoota;
    }
    public DcMotor getShootBmoter() {
        return shootb;
    }
    public DcMotor getLiftmoter() {
        return lift;
    }
}