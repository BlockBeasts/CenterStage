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

        while(true){
            telemetry.addData("velocity", robot.getShooterMotor().getVelocity());
            telemetry.update();
            robot.pause(50);
        }
    }
}
