package org.firstinspires.ftc.masters.test;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="Color Test", group = "Test")
public class  ColorTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        RevColorSensorV3 color = hardwareMap.get(RevColorSensorV3.class, "leftColor");
        RevColorSensorV3 colorMid = hardwareMap.get(RevColorSensorV3.class, "middleColor");
        RevColorSensorV3 colorRight = hardwareMap.get(RevColorSensorV3.class, "rightColor");

        waitForStart();


        int checkColorCount =0;
        int MAX_COUNT = 10;
        double redTotal=0, blueTotal=0, greenTotal=0;

        while(opModeIsActive()){

            String left = "Left distance: "+color.getDistance(DistanceUnit.MM)+" raw:"+color.rawOptical();
            String leftColor = " red:"+color.red()+" green:"+ color.green()+" blue:"+color.blue();

            String middle = "Middle distance: "+colorMid.getDistance(DistanceUnit.MM)+" raw:"+colorMid.rawOptical();
            String colorMiddle = " red:"+colorMid.red()+" green:"+ colorMid.green()+" blue:"+colorMid.blue();

            String right = " Left distance: "+colorRight.getDistance(DistanceUnit.MM)+" raw:"+colorRight.rawOptical();
            String rightcolor = " red:"+colorRight.red()+" green:"+ colorRight.green()+" blue:"+colorRight.blue();


           telemetry.addLine(left);
           telemetry.addLine(leftColor);
           telemetry.addLine(middle);
           telemetry.addLine(colorMiddle);
           telemetry.addLine(right);
           telemetry.addLine(rightcolor);
            telemetry.update();
        }
    }
}
