package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Date;

@Autonomous (name="Inside Blue Top Goal")
public class InsideBlueTop extends LinearOpMode {

    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot= new RobotClassInnerBlueOuterRed(hardwareMap, telemetry, this, "blue");

        robot.wobbleGoalGrippyThingGrab();
        // here you detect rings
        robot.openCVInnitShenanigans();

        RobotClass.RingPosition ringNmb = null;
        waitForStart();

        long startTime = new Date().getTime();
        long time = 0;

        while (time < 200 && opModeIsActive()) {
            time = new Date().getTime() - startTime;
            ringNmb = robot.analyze();

            telemetry.addData("Position", ringNmb);
            telemetry.update();
        }
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
        robot.forward(0.1,-0.3);
        robot.strafeLeft(0.4,1.3);
        robot.forward(0.5, -2.5);
        //shoot here please
        robot.pivotLeft(0.3, 28);
        //turn more. Originally twenty.
        robot.shoot3Autonomous();
        robot.pivotRight(0.3,28);

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.forward(0.5, -3.7);
            robot.strafeRight(0.5, 2);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,2.2);
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
//            here is the stuff for getting ring

            robot.forward(0.5, -6);
            robot.depositWobbleGoal();
            robot.forward(0.5, 2.5);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            robot.forward(0.5,-7.3);
            robot.strafeRight(0.5, 2.3);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,2.5);
            robot.forward(0.5,3.8);
        }

    }

    protected void shoot(){
        robot.shooterServo1(.8);
        robot.shooterServo2(.8);
        robot.pause(200);
        robot.intakeServoEngage(.9);
        robot.pause(800);
        robot.shooterServo2(0);
        robot.shooterServo1(0);
    }

}

