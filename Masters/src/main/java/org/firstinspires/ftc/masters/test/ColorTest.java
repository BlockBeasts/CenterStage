package org.firstinspires.ftc.masters.test;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Color Test", group = "Test")
@Disabled
public class ColorTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        RevColorSensorV3 color = hardwareMap.get(RevColorSensorV3.class, "color");

        waitForStart();

        int checkColorCount =0;
        int MAX_COUNT = 10;
        double redTotal=0, blueTotal=0, greenTotal=0;

        while(opModeIsActive()){
            if (checkColorCount<MAX_COUNT){
                redTotal+=color.red();
                blueTotal+=color.blue();
                greenTotal+=color.green();
                checkColorCount++;

                telemetry.addData("red total", redTotal);
                telemetry.addData("green total", greenTotal);
                telemetry.addData("blue total", blueTotal);
                telemetry.addData("count", checkColorCount);
            } else {

                if (redTotal>blueTotal && redTotal> greenTotal){
                    telemetry.addData("Color", "Red");
                }
                else if (greenTotal>blueTotal && greenTotal>redTotal){
                    telemetry.addData("Color", "yellow");
                } else if (blueTotal>greenTotal && blueTotal>redTotal){
                    telemetry.addData("Color", "Blue");
                }
            }

            telemetry.addData("raw", color.rawOptical());
            telemetry.addData("red", color.red());
            telemetry.addData("blue", color.blue());
            telemetry.addData("green", color.green());
            telemetry.update();
        }
    }
}
