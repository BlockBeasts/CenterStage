package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.masters.components.DriveTrain;
import org.firstinspires.ftc.masters.components.ITDCons;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config // Enables FTC Dashboard
@TeleOp
public class monitorTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static boolean colorRed = true;

    LynxGetADCCommand.Channel servoChannel;
    LynxGetADCCommand servoCommand;
    LynxGetADCResponse servoResponse;

    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Init init = new Init(hardwareMap);
        DriveTrain driveTrain = new DriveTrain(init, telemetry);

        Servo led = init.getLed();

        VoltageSensor controlHubVoltageSensor = init.getControlHubVoltageSensor();
        VoltageSensor expansionHubVoltageSensor = init.getExpansionHubVoltageSensor();

        DcMotorEx leftFrontMotor = init.getLeftFrontMotor();
        DcMotorEx rightFrontMotor = init.getRightFrontMotor();
        DcMotorEx leftRearMotor = init.getLeftRearMotor();
        DcMotorEx rightRearMotor = init.getRightRearMotor();
        DcMotorEx intakeMotor = init.getIntake();
        DcMotorEx intakeExtendo = init.getIntakeExtendo();
        DcMotorEx outtakeSlideLeft = init.getOuttakeSlideLeft();
        DcMotorEx outtakeSlideRight = init.getOuttakeSlideRight();

        LynxModule controlHublynx = init.getControlHublynx();
        LynxModule expansionHublynx = init.getExpansionHublynx();
        //LynxModule servoHublynx = init.getServoHublynx();

        waitForStart();

        while (opModeIsActive()) {

            driveTrain.driveNoMultiplier(gamepad1, DriveTrain.RestrictTo.XYT);

            if (colorRed) {
                led.setPosition(ITDCons.red);
            } else {
                led.setPosition(ITDCons.blue);
            }

            telemetry.addData("Control Hub Voltage", controlHubVoltageSensor.getVoltage());
            telemetry.addData("Expansion Hub Voltage", expansionHubVoltageSensor.getVoltage());

            telemetry.addData("Control Hub Current", controlHubVoltageSensor.getVoltage());
            telemetry.addData("Expansion Hub Current", expansionHubVoltageSensor.getVoltage());

            telemetry.addData("LeftFront Current", leftFrontMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RightFront Current", rightFrontMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("LeftRear Current", leftRearMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RightRear Current", rightRearMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("intakeMotor Current", intakeMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("intakeExtendo Current", intakeExtendo.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("outtakeLeft Current", outtakeSlideLeft.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("outtakeRight Current", outtakeSlideRight.getCurrent(CurrentUnit.AMPS));

            telemetry.addData("Control Hub Servo Current", getServoBusCurrent(controlHublynx));
            telemetry.addData("Expansion Hub Servo Current", getServoBusCurrent(expansionHublynx));
            //telemetry.addData("Servo Hub Servo Bus", getServoBusCurrent(servoHublynx));

            telemetry.update();

        }
    }

    double getServoBusCurrent(LynxModuleIntf device) {

        servoChannel = LynxGetADCCommand.Channel.SERVO_CURRENT;
        servoCommand = new LynxGetADCCommand(device, servoChannel, LynxGetADCCommand.Mode.ENGINEERING);
        try
        {
            servoResponse = servoCommand.sendReceive();
            return servoResponse.getValue() / 1000.0;    // return value in Amps
        }
        catch (InterruptedException | RuntimeException | LynxNackException e)
        {
        }
        return 999;
    }

}

