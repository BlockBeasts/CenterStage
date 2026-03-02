package org.firstinspires.ftc.masters.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Lift;

@Config
@TeleOp(name ="Foot Test")
public class FootTest extends LinearOpMode {

    public static int timeDelay = 100;
    @Override
    public void runOpMode() throws InterruptedException {

        Init init= new Init(hardwareMap);
        Lift lift = new Lift(init);

        ElapsedTime elapsedTime;
        waitForStart();
        elapsedTime = new ElapsedTime();
        while (opModeIsActive()){

            if (elapsedTime.milliseconds()<timeDelay){
                lift.lowerBot();
            } else{
                lift.stopLift();
            }

        }


    }
}
