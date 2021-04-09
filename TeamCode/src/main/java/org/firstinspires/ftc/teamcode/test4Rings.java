package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Date;

@Autonomous(name = "Test 4 Rings", group="Test")
public class test4Rings extends LinearOpMode{

    RobotClass robot;


    @Override
    public void runOpMode() {

        robot = new RobotClass(hardwareMap, telemetry, this, "blue");

        //robot.wobbleGoalGrippyThingGrab();

        // robot.innitDisplayTelemetryGyro();
        //robot.openCVInnitShenanigans();

        //  RobotClass.RingPosition ringNmb = null;

        waitForStart();

        robot.forward(.6, 1.1);
        robot.forward(0.2, -0.05);
        robot.intakeServoEngage(1);
        robot.startTimingBelt();
        robot.pause(600);
        robot.forward(0.05, 0.2);
        robot.pause(300);
        robot.forward(0.05, 0.2);
        robot.pause(300);
        robot.forward(0.05, 0.2);
        robot.pause(300);

        robot.pause(1000);
        robot.stopTimingBelt();

    }


}
