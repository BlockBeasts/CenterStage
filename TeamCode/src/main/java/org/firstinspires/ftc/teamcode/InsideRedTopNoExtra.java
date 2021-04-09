package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Date;

@Autonomous (name="Inside Red Top Goal No Extra", group="Red")
public class InsideRedTopNoExtra extends LinearOpMode {

    DcMotor wobbleGoalExtendMotor = null;
    DcMotor wobbleGoalRaiseMotor = null;
    Servo wobbleGoalGrippyThing = null;
    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {

        wobbleGoalRaiseMotor = hardwareMap.dcMotor.get("wobbleLift");
        wobbleGoalGrippyThing = hardwareMap.servo.get("wobbleGrip");
        robot= new RobotClass(hardwareMap, telemetry, this, "red");

        robot.wobbleGoalGrippyThingGrab();
        // here you detect rings
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
        robot.strafeRight(0.5, 2.0);
        robot.forward(.5, -2.5);
        double angle = robot.getAngleFromGyro();
        telemetry.addData("angle", angle);
        telemetry.update();
        if (angle < 1) {
            robot.pivotLeft(.1, -angle+1);
        }

        robot.shoot3Autonomous();

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.forward(0.5,-2.5);
            robot.strafeLeft(0.5,2);
            robot.pivotLeft(0.3, 170);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.7, 2);
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
            robot.forward(0.5, -4);
            robot.strafeLeft(0.3, 0.2);
            robot.pivotRight(0.4, 170);
            robot.depositWobbleGoal();
            robot.forward(0.7, -1.8);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            robot.forward(0.8,-4.5);
            robot.strafeLeft(0.8,3.2);
            robot.pivotLeft(0.4,170);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.8,1);
            robot.forward(0.8,-2.8);
        }

    }

}

