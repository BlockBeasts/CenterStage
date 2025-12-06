package org.firstinspires.ftc.masters;

import static org.firstinspires.ftc.masters.quickAndDirtyTeleOp.MOTOR_VELO_PID;
import static org.firstinspires.ftc.masters.quickAndDirtyTeleOp.targetVelo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Config // Enables FTC Dashboard
@Autonomous(name = "Auto Timing Test")
public class AutoTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private VoltageSensor batteryVoltageSensor;
    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;

    public static long wait1 = 500;
    public static long wait2 = 500;
    public static long wait3 = 500;
    boolean done;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();


        shoot = hardwareMap.get(DcMotorImplEx.class, "shooter");
        pusher1 = hardwareMap.crservo.get("pusher1");
        pusher2 = hardwareMap.crservo.get("pusher2");

        shoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        setPIDFCoefficients(shoot, MOTOR_VELO_PID);

double currentVelo;

        waitForStart();

            currentVelo = shoot.getVelocity();


            if(!done) {
                shoot.setVelocity(targetVelo);

                //shoot 1
                while (shoot.getVelocity()<1380 && opModeIsActive()){

                }

                pusher1.setPower(1);
                pusher2.setPower(-1);
                sleep(wait1);
                pusher1.setPower(0);
                pusher2.setPower(0);

                //shoot 2
                while (shoot.getVelocity()<1380 && opModeIsActive()){

                }
                pusher1.setPower(1);
                pusher2.setPower(-1);
                sleep(wait2);
                pusher1.setPower(0);
                pusher2.setPower(0);

                //shoot 3
                while (shoot.getVelocity()<1380 && opModeIsActive()){

                }
                pusher1.setPower(1);
                pusher2.setPower(-1);
                sleep(wait3);
                pusher1.setPower(0);
                pusher2.setPower(0);

                done = true;
                shoot.setPower(0);

        }
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

