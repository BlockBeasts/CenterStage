package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Date;

@Autonomous(name = "Outer Blue No Extra", group = "Blue")
public class OuterBlueAutonomousNoExtra extends LinearOpMode{

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
        if (angle<1){
            robot.pivotLeft(.1, Math.abs(angle+1));
        }
        robot.shoot3Autonomous();

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.intakeServoStop();
            robot.shooterStop();
            robot.intakeServoStop();
            robot.forward(.5, -0.7);
            robot.strafeLeft(.5, 2.1);
            robot.pause(8000);
            robot.forward(.5, -2.2);
            robot.depositWobbleGoal();
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
            robot.intakeServoStop();
            robot.shooterStop();
            robot.intakeServoStop();
            robot.forward(.6,-5);
            robot.pivotRight(.4,170);
            robot.depositWobbleGoal();
            robot.forward(0.5,-1.8);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            robot.forward(.6,-5.5);
            robot.pivotRight(.4,90);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,3);
        }
    }

}
