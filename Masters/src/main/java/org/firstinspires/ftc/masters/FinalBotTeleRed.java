package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.components.Constant;

@TeleOp(name = "Decode Teleop Red")
public class FinalBotTeleRed extends FinalBotTeleBlue {



    boolean lifted = false;

//    ElapsedTime runtime;

    @Override
    public void initializeHardwareAlliance(){

        allianceColor = Constant.AllianceColor.RED;

    }

}
