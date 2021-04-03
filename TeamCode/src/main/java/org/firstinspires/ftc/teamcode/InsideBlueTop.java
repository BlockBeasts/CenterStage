package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Date;

@Autonomous (name="Inside Blue Top Goal")
public class InsideBlueTop extends LinearOpMode {

    DcMotor wobbleGoalExtendMotor = null;
    DcMotor wobbleGoalRaiseMotor = null;
    Servo wobbleGoalGrippyThing = null;
    RobotClass robot;

    @Override
    public void runOpMode() throws InterruptedException {

        // wobbleGoalExtendMotor = hardwareMap.dcMotor.get("wobbleExtendo");
        wobbleGoalRaiseMotor = hardwareMap.dcMotor.get("wobbleLift");
        wobbleGoalGrippyThing = hardwareMap.servo.get("wobbleGrip");
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

        robot.forward(0.1,-0.3);
        robot.strafeLeft(0.4,1.3);
        robot.forward(0.5, -4.1);
        //shoot here please
        robot.pivotLeft(0.3, 20);
        robot.shooterEngageAlt();
        robot.pause(1000);
        shoot();
        shoot();
        shoot();
        robot.intakeServoStop();
        robot.stopShooting();
        robot.pivotRight(0.3,20);

        if (ringNmb == RobotClass.RingPosition.NONE) {
            robot.forward(0.5, -1.7);
            robot.strafeRight(0.5, 2);
            robot.depositWobbleGoal();
            robot.strafeLeft(0.5,2.2);
        } else if (ringNmb == RobotClass.RingPosition.ONE) {
//            here is the stuff for getting ring

            robot.forward(0.5, -4);
            robot.depositWobbleGoal();
            robot.forward(0.5, 2.5);
        } else if (ringNmb == RobotClass.RingPosition.FOUR) {
            robot.forward(0.5,-5.3);
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

