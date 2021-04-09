package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Date;

@Autonomous(name = "Outer Blue", group="Blue")
public class OuterBlueAutonomous extends LinearOpMode{

    RobotClass robot;


    @Override
    public void runOpMode() {

        robot= new RobotClass(hardwareMap, telemetry, this, "blue");

        robot.wobbleGoalGrippyThingGrab();
        robot.openCVInnitShenanigans();

        RobotClass.RingPosition ringNmb = null;

        waitForStart();
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
        robot.forward(0.2, -0.3);
        robot.strafeLeft(0.5, 1.7);
        long startTime = new Date().getTime();
        long time = 0;

        while (time < 500 && opModeIsActive()) {
            time = new Date().getTime() - startTime;
            ringNmb = robot.analyze();

            telemetry.addData("Position", ringNmb);
            telemetry.update();
        }
        robot.strafeRight(0.5,1.9);
        robot.forward(.5,-2.5);
        double angle= robot.getAngleFromGyro();
        telemetry.addData("angle", angle);
        telemetry.update();
        if (angle<0){
            robot.pivotLeft(.1, -angle);
        }

        robot.shoot3Autonomous();

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.forward(.5, -0.7);
            robot.strafeLeft(.5, 2.1);
            robot.pause(8000);
            robot.forward(.5, -2.3);
            robot.depositWobbleGoal();
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
           robot.intakeServoEngage(1);
           robot.startTimingBelt();
            robot.forward(.3, -.3);
            robot.pivotLeftSloppy(.8, 65);
            angle= robot.getAngleFromGyro();
            robot.pivotLeft(.3, 90-Math.abs(angle));
            //get the ring
            robot.forward(.7, 1.5);
            robot.stopTimingBelt();
            robot.pivotRight(0.5,80);
            robot.startTimingBelt();
            robot.pause(2500);
            robot.shooterStop();
            robot.pivotRight(0.5, 100);
            robot.strafeRight(0.6, 4);
            robot.forward(0.6,-0.7);
           robot.depositWobbleGoal();
             robot.strafeLeft(0.6,0.5);

        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            robot.intakeServoEngage(1);
            robot.startTimingBelt();
            robot.forward(.3, -.3);
            robot.pause(300);
            robot.pivotLeftSloppy(.8, 65);
            angle= robot.getAngleFromGyro();
            robot.pivotLeft(.3, 90-Math.abs(angle));
            //get the ring
            robot.forward(.7, 1.5);
            //robot.pause(100);
            robot.stopTimingBelt();
            robot.pivotRight(0.5,80);
            robot.startTimingBelt();
            robot.pause(2500);
            robot.shooterStop();
            robot.stopTimingBelt();
            robot.intakeServoStop();
            angle= robot.getAngleFromGyro();
            robot.pivotRight(0.2, Math.abs(angle));

            robot.forward(.6,-6.7);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,1);
            robot.forward(0.8,3.7);
        }
    }

}
