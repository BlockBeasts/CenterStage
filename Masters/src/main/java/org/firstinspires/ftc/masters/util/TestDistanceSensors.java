package org.firstinspires.ftc.masters.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.RobotClass;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//@Disabled
@Autonomous(name = "Distance sensors test thing", group="test")
public class TestDistanceSensors extends LinearOpMode {
    RobotClass robot;

    @Override
    public void runOpMode () {
        robot = new RobotClass(hardwareMap, telemetry, this);

        waitForStart();

        while (true && opModeIsActive()) {
            telemetry.addData("intake", robot.distanceSensorIntake.getDistance(DistanceUnit.CM));
//            telemetry.addData("top sensor distance = ", robot.intakeColor.getDistance(DistanceUnit.CM));
            telemetry.update();
            sleep(50);
        }

    }
}