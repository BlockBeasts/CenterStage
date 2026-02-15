//package org.firstinspires.ftc.masters.components;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//public class Init2 {
//
//    DcMotorEx frontLeft;
//    DcMotorEx backLeft;
//    DcMotorEx frontRight;
//    DcMotorEx backRight;
//
//    DcMotorEx leftMotor;
//
//    DcMotorEx rightMotor;
//
//    DcMotorEx intakeMotor;
//
//    DcMotor lift;
//
//    Servo outakeLiftLeft;
//
//    Servo outakeLiftRight;
//
//    Servo outakeLiftMiddle;
//
//    Servo leftLight;
//    Servo middleLight;
//    Servo rightLight;
//
//    Servo leftServo;
//    Servo rightServo;
//
//
//
//    public Init2(HardwareMap hardwareMap) {
//
//        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
//        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
//        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
//        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
//
//        leftMotor = hardwareMap.get(DcMotorEx.class, "leftMotor");
//        rightMotor = hardwareMap.get(DcMotorEx.class, "rightMotor");
//
//
//        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
//
//
//        lift = hardwareMap.dcMotor.get("lift");
//
//        outakeLiftLeft = hardwareMap.get(Servo.class, "trayLeftServo");
//        outakeLiftMiddle = hardwareMap.get(Servo.class, "trayMiddleServo");
//        outakeLiftRight = hardwareMap.get(Servo.class, "trayRightServo");
//
//        leftServo = hardwareMap.get(Servo.class, "leftServo");
//        rightServo = hardwareMap.get(Servo.class, "rightServo");
//
//        leftLight = hardwareMap.get(Servo.class, "leftColor");
//        middleLight = hardwareMap.get(Servo.class, "middleColor");
//        rightLight = hardwareMap.get(Servo.class, "rightColor");
//
//        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//
//    }
//
//    public DcMotorEx getFrontLeft() {
//        return frontLeft;
//    }
//
//    public DcMotorEx getBackLeft() {
//        return backLeft;
//    }
//
//    public DcMotorEx getFrontRight() {
//        return frontRight;
//    }
//
//    public DcMotorEx getBackRight() {
//        return backRight;
//    }
//
//    public DcMotorEx getIntakeMotor() {
//        return intakeMotor;
//    }
//    public DcMotor getLiftMotor() {
//        return lift;
//    }
//    public DcMotorEx getleftMotor() {
//        return leftMotor;
//    }
//
//    public DcMotorEx getrightMotor() {
//        return rightMotor;
//    }
//    public Servo getliftLeftServo() {
//        return outakeLiftLeft;
//    }
//    public Servo getliftRightServo() {
//        return outakeLiftRight;
//    }
//    public Servo liftMiddleServo() {
//        return outakeLiftMiddle;
//    }
//
//    public Servo getleftServoMotor() {
//        return leftServo;
//    }
//
//    public Servo getrightServoMotor() {
//        return rightServo;
//    }
//
//}
