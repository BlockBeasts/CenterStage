package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class EncodeTest extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {

        CRServo leftFeed= hardwareMap.crservo.get("leftFeedServo");
        CRServo righTFeed = hardwareMap.crservo.get("FeedServoRight");
        DcMotorEx launcher = hardwareMap.get(DcMotorEx.class, "Launcher");

        waitForStart();

        if (gamepad1.a){
            leftFeed.setPower(1);
            righTFeed.setPower(-1);
        } else if (gamepad1.b){
            leftFeed.setPower(0);
            righTFeed.setPower(0);
        }

        if (gamepad1.x){
            launcher.setPower(1);
        } else if (gamepad1.y){
            launcher.setPower(0);
        }
    }
}
