package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Date;

@Autonomous(name = "Outer Red No Extra", group="Red")
public class OuterRedAutonomousNoExtra extends LinearOpMode {

    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot= new RobotClassInnerBlueOuterRed(hardwareMap, telemetry, this, "red");
        robot.wobbleGoalGrippyThingGrab();
        robot.openCVInnitShenanigans();

        RobotClass.RingPosition ringNmb = null;

        waitForStart();
        long startTime = new Date().getTime();
        long time = 0;

        while (time < 300 && opModeIsActive()) {
            time = new Date().getTime() - startTime;
            ringNmb = robot.analyze();

            telemetry.addData("Position", ringNmb);
            telemetry.update();
        }
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
        robot.forward(0.1,-0.3);
        robot.strafeLeft(0.3,0.9);
        robot.forward(0.5, -2.5);
        //shoot here please
        robot.pivotLeftSloppy(0.6, 15);
        double angle = robot.getAngleFromGyro();
        robot.pivotLeft(0.3, 27- Math.abs(angle));
        //turn more. Originally twenty.
        robot.shoot3Autonomous();

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.shotAllshooting();
            robot.pivotRight(0.3, Math.abs(robot.getAngleFromGyro())+90);
            robot.strafeRight(0.5, 2.4);
            robot.forward(0.5, -0.3);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5, 0.5);
            robot.forward(0.5, 2);
            robot.pause(6000);
            robot.strafeRight(0.5, 2);
        } else if (ringNmb == RobotClass.RingPosition.ONE) {

            robot.pivotRight(.5, 27);
            robot.forward(.4,-6.5);
            robot.depositWobbleGoal();
            robot.forward(0.5, 2);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
          //  robot.pause(4000);
            robot.pivotRight(.3, 27);
            robot.forward(.5,-6.15);
            robot.pivotRight(0.4,90);
            robot.pause(500);
           // robot.forward(0.2,-1);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,2.2);
        }

    }
}
