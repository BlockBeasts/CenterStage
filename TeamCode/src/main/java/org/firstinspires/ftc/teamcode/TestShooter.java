package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

@TeleOp(name="Test Shooter")
public class TestShooter extends LinearOpMode {

    RobotClass robot;
    @Override
    public void runOpMode() {
        robot= new RobotClassInnerBlueOuterRed(hardwareMap, telemetry, this, "blue");
        waitForStart();

        robot.shooterEngage();
        robot.shooterServo1(1);
        robot.shooterServo2(1);
        robot.intakeServoEngage(1);
        robot.pause(1000);
        robot.stopTimingBelt();
        robot.shooterEngage();
        robot.shooterServo1(1);
        robot.shooterServo2(1);
        robot.pause(800);
        robot.stopTimingBelt();
        robot.intakeServoStop();
        robot.shooterEngage();
        robot.shooterServo1(1);
        robot.shooterServo2(1);
        robot.pause(1000);
        robot.stopShooting();





        while(opModeIsActive()){
            telemetry.addData("velocity", robot.getShooterMotor().getVelocity());
            telemetry.update();
            robot.pause(50);
        }
    }
}
