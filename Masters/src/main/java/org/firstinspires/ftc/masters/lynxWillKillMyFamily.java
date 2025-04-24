package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.masters.components.ITDCons;

@Config // Enables FTC Dashboard
@TeleOp(name = "LynxGonnaKillMe")
public class lynxWillKillMyFamily extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double Blank = 0;

    Servo led;

    LynxModule myRevHub;
    LynxGetADCCommand.Channel servoChannel;
    LynxGetADCCommand servoCommand;
    LynxGetADCResponse servoResponse;
    double servoBusCurrent;

    public void runOpMode() throws InterruptedException {

        led = hardwareMap.servo.get("led");
        myRevHub = hardwareMap.get(LynxModule.class, "Control Hub");
        servoBusCurrent = getServoBusCurrent();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            led.setPosition(ITDCons.red);

            telemetry.addData("Control Hub Servo Bus", getServoBusCurrent());
            telemetry.update();

        }
    }

    double getServoBusCurrent()
    {
        servoChannel = LynxGetADCCommand.Channel.SERVO_CURRENT;
        servoCommand = new LynxGetADCCommand(myRevHub, servoChannel, LynxGetADCCommand.Mode.ENGINEERING);
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

