package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Mecanum Test?")
public class MyFirstMecanumOpMode_Linear extends LinearOpMode {
    //    private Gyroscope imu;
//    private DcMotor motorTest;
//    private DigitalChannel digitalTouch;
//    private DistanceSensor sensorColorRange;
//    private Servo servoTest;
    DcMotor leftFrontMotor = null;
    DcMotor rightFrontMotor = null;
    DcMotor leftRearMotor = null;
    DcMotor rightRearMotor = null;
  //  DcMotor wobbleGoalExtendMotor = null;
    DcMotor wobbleGoalRaiseMotor = null;
    DcMotorImplEx shooterMotor = null;
    Servo wobbleGoalGrippyThing = null;
    CRServo intakeOne = null;
  //  CRServo intakeTwo = null;
    CRServo shooterServo1 = null;
    CRServo shooterServo2 = null;
    RobotClass robot;

    private double ticks = 537;//537


    @Override
    public void runOpMode() {
//        imu = hardwareMap.get(Gyroscope.class, "imu");
//        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
//        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
//        servoTest = hardwareMap.get(Servo.class, "servoTest");
        robot= new RobotClass(hardwareMap, telemetry, this);

        leftFrontMotor = hardwareMap.dcMotor.get("frontLeft");
        rightFrontMotor = hardwareMap.dcMotor.get("frontRight");
        leftRearMotor = hardwareMap.dcMotor.get("backLeft");
        rightRearMotor = hardwareMap.dcMotor.get("backRight");
       // wobbleGoalExtendMotor = hardwareMap.dcMotor.get("wobbleExtendo");
        wobbleGoalRaiseMotor = hardwareMap.dcMotor.get("wobbleLift");
        shooterMotor = (DcMotorImplEx) hardwareMap.dcMotor.get("shooterMotor");
        wobbleGoalGrippyThing = hardwareMap.servo.get("wobbleGrip");
        intakeOne = hardwareMap.crservo.get("intakeServoOne");
      //  intakeTwo = hardwareMap.crservo.get("intakeServoTwo");
        shooterServo1 = hardwareMap.crservo.get("shooterServo1");
        shooterServo2 = hardwareMap.crservo.get("shooterServo2");

        leftFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftRearMotor.setDirection(DcMotor.Direction.FORWARD);
        rightRearMotor.setDirection(DcMotor.Direction.REVERSE);
     //   intakeTwo.setDirection(CRServo.Direction.REVERSE);

        boolean yPressed = false;
        boolean yOpen = true;
        boolean shooterServoPressed = false;
        boolean shooterServoOn= false;
        boolean intake = false;
        wobbleGoalGrippyThing.setPosition(0.9);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double leftFrontPower = y + x - rx;
            double leftRearPower = y - x - rx;
            double rightFrontPower = y - x + rx;
            double rightRearPower = y + x + rx;

            if (Math.abs(leftFrontPower) > 1 || Math.abs(leftRearPower) > 1 || Math.abs(rightFrontPower) > 1 || Math.abs(rightRearPower) > 1) {

                double max;
                max = Math.max(leftFrontPower, leftRearPower);
                max = Math.max(max, rightFrontPower);
                max = Math.max(max, rightRearPower);

                leftFrontPower /= max;
                leftRearPower /= max;
                rightFrontPower /= max;
                rightRearPower /= max;
            }

            leftFrontMotor.setPower(leftFrontPower);
            leftRearMotor.setPower(leftRearPower);
            rightFrontMotor.setPower(rightFrontPower);
            rightRearMotor.setPower(rightRearPower);

            if (gamepad2.dpad_down) {
                if (!shooterServoPressed) {
                    if (shooterServoOn) {
                        shooterServo1.setPower(0);
                        shooterServo2.setPower(0);
                        shooterServoOn = false;
                    } else {
                        shooterServo1.setPower(0.8);
                        shooterServo2.setPower(0.8);
                        shooterServoOn = true;
                    }
                }
                shooterServoPressed=true;
            } else {
                shooterServoPressed = false;
            }


            if (gamepad2.left_trigger >= .87) {
                wobbleGoalRaiseMotor.setPower(.6);
            } else if (gamepad2.left_bumper == true) {
                wobbleGoalRaiseMotor.setPower(-.4);
            } else {
                wobbleGoalRaiseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                wobbleGoalRaiseMotor.setPower(0);
            }

            if (gamepad2.right_trigger >= .87) {
                shooterMotor.setVelocity(-5400*0.85*28/60);
            } else if(gamepad2.right_bumper == true){
                shooterMotor.setVelocity(0);
            }


//            if (gamepad2.right_bumper == true) {
//                wobbleGoalExtendMotor.setPower(-.5);
//            } else {
//                wobbleGoalExtendMotor.setPower(0);
//            }

            if (gamepad2.a) {
                intakeOne.setPower(0.9);
            } else if (gamepad2.b) {
                intakeOne.setPower(-0.9);
            } else if (gamepad2.x){
                intakeOne.setPower(0);
            }
            /*beep boop
            *
            * robot thigd
            *
            *
            * */
            if (gamepad2.y) {
                if (!yPressed) {
                    if (yOpen) {
                        wobbleGoalGrippyThing.setPosition(0.2);
                        yOpen = false;
                    } else {
                        wobbleGoalGrippyThing.setPosition(.9);
                        yOpen = true;
                    }
                }
                yPressed=true;
            } else {
                yPressed = false;
            }
            if (gamepad1.a) {
                forwardToWhite(.9,.5,.3);
                forward(.5, -2.7);
            }

        }
    }
    public void forwardToWhite (double speed, double rotations, double speed2) {
        robot.frontLeft.setPower(speed2);
        robot.frontRight.setPower(speed2);
        robot.backLeft.setPower(speed2);
        robot.backRight.setPower(speed2);

        while (robot.colorSensor.alpha() < 600 && !gamepad1.x) {

            telemetry.addData("Light Level: ", robot.colorSensor.alpha());
            telemetry.update();
        }

        robot.stopMotors();
    }
    public void forward (double speed, double rotations){
        int leftCurrent = robot.frontLeft.getCurrentPosition();
        int rightCurrent = robot.frontRight.getCurrentPosition();
        int backLeftCurrent = robot.backLeft.getCurrentPosition();
        int backRightCurrent = robot.backRight.getCurrentPosition();

        telemetry.addData("Target Front Left Motor Position", leftCurrent);
        telemetry.addData("Target Front Right Motor Position", rightCurrent);
        telemetry.addData("Target Back Left Motor Position", backLeftCurrent);
        telemetry.addData("Target Back Right Motor Position", backRightCurrent);
        telemetry.update();

        double toPositionLeft = leftCurrent + rotations*ticks;
        double toPositionRight = rightCurrent + rotations*ticks;
        double toPositionbackLeft = backLeftCurrent + rotations*ticks;
        double toPositionbackRight = backRightCurrent + rotations*ticks;

        telemetry.addData("Target Front Left Motor Position", toPositionLeft);
        telemetry.addData("Target Front Right Motor Position", toPositionRight);
        telemetry.addData("Target Back Left Motor Position", toPositionbackLeft);
        telemetry.addData("Target Front Left Motor Position", toPositionbackLeft);
        telemetry.update();

        robot.frontLeft.setTargetPosition((int)toPositionLeft);
        robot.frontRight.setTargetPosition((int)toPositionRight);
        robot.backLeft.setTargetPosition((int)toPositionbackLeft);
        robot.backRight.setTargetPosition((int)toPositionbackRight);

        robot.motorSetMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.frontLeft.setPower(Math.abs(speed));
        robot.frontRight.setPower(Math.abs(speed));
        robot.backLeft.setPower(Math.abs(speed));
        robot.backRight.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) && !gamepad1.x) {

            // Display it for the driver.
            robot.motorTelemetry();
        }
        robot.stopMotors();

        robot.motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}
