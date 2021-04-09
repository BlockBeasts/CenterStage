package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Date;

@Autonomous(name = "Outer Red", group="Red")
public class OuterRedAutonomous extends LinearOpMode {

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
            shootExtra();

            robot.strafeLeft(0.6, 1.7);
            robot.forward(0.8,-5.2);
            robot.depositWobbleGoal();
            robot.forward(0.8, 1.8);

        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            shootExtra();

            robot.strafeLeft(0.6, 2.0);
            robot.forward(0.8,-5);
            robot.pivotRightSloppy(0.7,90);
            robot.pivotRight(0.5, 120- Math.abs(robot.getAngleFromGyro()));
            robot.depositWobbleGoal();
            robot.pivotLeft(0.6, Math.abs(robot.getAngleFromGyro())-90);
            robot.strafeLeft(0.8,2.2);

//            robot.pivotRight(.3, 24);
//            robot.forward(.5,-6.15);
//            robot.pivotRight(0.4,90);
//            robot.pause(500);
//           // robot.forward(0.2,-1);
//            robot.depositWobbleGoal();
//            robot.strafeLeft(0.5,2.2);
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
      //  robot.pause(400);
        robot.pivotLeftSloppy(0.7, 75);
        robot.stopTimingBelt();
        angle = robot.getAngleFromGyro();
        angle=18-angle;
        robot.pivotLeft(0.5,angle);
        robot.getShooterMotor().setVelocity(robot.TOP_TARGET_SPEED);
        robot.startTimingBelt();
        //robot.forward(0.1,0.2);
        telemetry.addData("angle", robot.getAngleFromGyro());
        telemetry.update();
        robot.pause(2300);
        robot.shotAllshooting();
        robot.pivotRight(0.5, Math.abs(robot.getAngleFromGyro())+2);
    }
}
