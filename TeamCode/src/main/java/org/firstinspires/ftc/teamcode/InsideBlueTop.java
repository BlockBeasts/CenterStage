package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.opencv.core.Mat;

import java.util.Date;

@Autonomous (name="Inside Blue Top Goal", group="Blue")
public class InsideBlueTop extends LinearOpMode {

    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot= new RobotClassInnerBlueOuterRed(hardwareMap, telemetry, this, "blue");

        robot.wobbleGoalGrippyThingGrab();
        // here you detect rings
        robot.openCVInnitShenanigans();

        RobotClass.RingPosition ringNmb = RobotClass.RingPosition.NONE;
        waitForStart();

        long startTime = new Date().getTime();
        long time = 0;

        while (time < 500 && opModeIsActive()) {
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
            robot.pivotRight(0.3, Math.abs(robot.getAngleFromGyro()));
            robot.forward(0.5, -3.7);
            robot.strafeRight(0.5, 2);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,2.2);
            robot.forward(0.2, 0.3);
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
            shootExtra();
            robot.strafeLeft(0.6, 1.9);
            robot.forward(0.8,-4.8);
            robot.depositWobbleGoal();
            robot.forward(0.8, 2.3);
        } else if (ringNmb== RobotClass.RingPosition.FOUR){
            shootExtra();
            robot.strafeLeft(0.7, 0.4);
            robot.forward(0.7,-7.4);
            robot.strafeRight(0.8, 0.5);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.9, 0.6);
            robot.forward(0.9, 3);
        }
    }

    protected void shootExtra(){
        robot.startTimingBelt();
        robot.intakeServoEngage(1);
        robot.pivotRightSloppy(0.7, 70);
        double angle = robot.getAngleFromGyro();
        robot.pivotRight(0.3,90-Math.abs(angle));
        robot.strafeRight(0.5, 0.4);
        robot.forward(.7, 1.4);
        //robot.pause(400);
        robot.pivotLeftSloppy(0.7, 75);
        robot.stopTimingBelt();
        angle = robot.getAngleFromGyro();
        angle=14-angle;
        robot.pivotLeft(0.5,angle);
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
        robot.startTimingBelt();
        robot.forward(0.1,0.2);
        telemetry.addData("angle", robot.getAngleFromGyro());
        telemetry.update();
        robot.pause(2200);
        robot.shotAllshooting();
        robot.pivotRight(0.5, Math.abs(robot.getAngleFromGyro())+2);
    }

}

