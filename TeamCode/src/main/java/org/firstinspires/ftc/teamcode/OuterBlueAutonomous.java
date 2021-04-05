package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Date;

@Autonomous(name = "OuterBlueAutonomous")
public class OuterBlueAutonomous extends LinearOpMode{

    RobotClass robot;


    @Override
    public void runOpMode() {

        robot= new RobotClass(hardwareMap, telemetry, this, "blue");

        robot.wobbleGoalGrippyThingGrab();

       // robot.innitDisplayTelemetryGyro();
        robot.openCVInnitShenanigans();

        RobotClass.RingPosition ringNmb = null;

        waitForStart();

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
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
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
            robot.forward(.5, -2.4);
            //robot.pivotRight(.4, 15);
            robot.depositWobbleGoal();
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
           robot.intakeServoEngage(1);
           robot.startTimingBelt();
            robot.forward(.3, -.3);
            robot.pivotLeftSloppy(.8, 65);
            angle= robot.getAngleFromGyro();
            robot.pivotLeft(.3, 90-Math.abs(angle));
            robot.forward(.6, 1.5);

            robot.forward(.6, -1.2);

            angle= robot.getAngleFromGyro();
            telemetry.addData("angle", angle);
            telemetry.update();
            robot.pivotRightSloppy(.8, 60);
            robot.stopTimingBelt();
            angle= robot.getAngleFromGyro();
            telemetry.addData("angle", angle);
            telemetry.update();
            robot.pivotRight(.3, Math.abs(angle));

            robot.forward(0.3, .3);
            if (angle<0){
                robot.pivotLeft(.05, -angle);
            }
            robot.shooterEngageAndMove(800,400);
            robot.shooterStop();
            robot.shooterServo1Stop();
            robot.shooterServo2Stop();
            robot.intakeServoStop();
            angle= robot.getAngleFromGyro();
            if (angle>0){
                robot.pivotRight(.1, angle);
            }
            robot.forward(.8,-4.8);
            robot.pivotRightSloppy(.7,130);
            angle= robot.getAngleFromGyro();
            telemetry.addData("angle", robot.getAngleFromGyro());
            telemetry.update();
            robot.pivotRight(.2,175-Math.abs(angle));
            robot.depositWobbleGoal();
           robot.forward(0.8,-1.5);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {

            robot.forward(.6,-5.7);
            robot.pivotRight(.4,90);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,3);

        }
    }

}
