package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.components.Init;

@Config // Enables FTC Dashboard
@TeleOp(name = "QuickAndDirtyTeleOp")
public class quickAndDirtyTeleOp extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Init init;

    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;

    Servo indicator;

    ElapsedTime shootTime;
    public static int shootTimeVar;

    boolean shooter;

    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(800, 0, 5, 12);


    private VoltageSensor batteryVoltageSensor;


    public static double Blank = 0;

    public static int targetVelo = 1375;
    public static double currentVelo = 0;

    public void runOpMode() throws InterruptedException {

        telemetry.update();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        shooter = false;

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        shoot = hardwareMap.get(DcMotorImplEx.class, "shooter");
        pusher1 = hardwareMap.crservo.get("pusher1");
        pusher2 = hardwareMap.crservo.get("pusher2");

        indicator = hardwareMap.get(Servo.class, "indicator");

        shootTime = new ElapsedTime();


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        MotorConfigurationType motorConfigurationType = shoot.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        shoot.setMotorType(motorConfigurationType);

        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
//        setPIDFCoefficients(shoot, MOTOR_VELO_PID);

        //   TuningController tuningController = new TuningController();

        double lastKp = 0.0;
        double lastKi = 0.0;
        double lastKd = 0.0;
        double lastKf = getMotorVelocityF();


//        tuningController.start();


        waitForStart();

        while (opModeIsActive()) {
            currentVelo = shoot.getVelocity();

            if (gamepad2.dpad_up) {


                shooter = true;

            }
            if (gamepad2.dpad_down) {


                shooter = false;

            }


            if (shooter) {

                shoot.setVelocity(targetVelo);

                telemetry.addData("targetVelocity", targetVelo);


            } else {
                shoot.setVelocity(0);
            }
            telemetry.addData("currentVelocity", currentVelo);
            telemetry.update();


            cartesianDrive(Math.pow(gamepad1.left_stick_x, 3), -Math.pow(gamepad1.left_stick_y, 3), Math.pow((gamepad1.right_trigger * .8) - (gamepad1.left_trigger * .8), 3));


            if (gamepad2.a) {
                shootTime = new ElapsedTime();
                pusher1.setPower(1);
                pusher2.setPower(-1);
            } else if (shootTime.milliseconds() >= shootTimeVar) {
                pusher1.setPower(0);
                pusher2.setPower(0);
            }

            if (currentVelo >= 1370) {
                indicator.setPosition(.500);
            } else {
                indicator.setPosition(.28);
            }


        }
    }

    public void cartesianDrive(double x, double y, double t) {

        if (Math.abs(y) < 0.2) {
            y = 0;
        }
        if (Math.abs(x) < 0.2) {
            x = 0;
        }

        double leftFrontPower = y + x + t;
        double leftRearPower = y - x + t;
        double rightFrontPower = y - x - t;
        double rightRearPower = y + x - t;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(t), 1);

        leftFrontPower /= denominator;
        leftRearPower /= denominator;
        rightFrontPower /= denominator;
        rightRearPower /= denominator;

        frontLeft.setPower(leftFrontPower);
        backLeft.setPower(leftRearPower);
        frontRight.setPower(rightFrontPower);
        backRight.setPower(rightRearPower);
    }


    private void setPIDFCoefficients(DcMotorEx motor, PIDFCoefficients coefficients) {
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d, coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        ));
    }

    public static double getMotorVelocityF() {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 * 60.0 / (TuningController.MOTOR_MAX_RPM * TuningController.MOTOR_TICKS_PER_REV);
    }

}

