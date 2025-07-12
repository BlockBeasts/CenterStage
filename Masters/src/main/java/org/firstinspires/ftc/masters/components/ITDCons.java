package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

@Config
public class ITDCons {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public enum Color {red, blue, yellow, unknown}

    public static double zero = 0;

    public static double intakeInit = 0.5;
    public static double intakeInitLeft = 0;
    public static double intakeInitRight = 1;

    //public static double intakeArmDrop =0.88; Former Axon
    public static double intakeArmDrop = 0.9;
    public static double intakeChainDrop = 0.93;

    public static double intakeArmNeutral= 0.67;
    public static double intakeChainNeutral=0.93;

    public static double intakeArmShoot= 0.4;
    public static double intakeChainShoot=0.2;

    //public static double intakeArmTransfer=0.33; Former Axon
    public static double intakeArmTransfer=0.55;
    public static double intakeChainTransfer = 0.93;
    public static double intakeTransferSpeed = 1;
    public static double intakeEjectSpeed = 1;

    public static double pushIn=0.35;
    public static double pushOut=0.575;

    public static double clawOpenTransfer =0.3;
    public static double clawOpen = 0.3;
    public static double clawClose = 0.58;



    public static double wristFront= 0.03;
    public static double wristBack = 0.75;

    public static double positionBack = 0.09;
    public static double positionInitSpec=0.4;
    public static double positionTransfer = 0.36;
    public static double positionFront =1;

    public static double angleBack = 0.07;
    public static double angleTransferArm = 0.1;
    public static double angleTransferClaw = 0.7;

    public static double angleMiddle = 0.55;
    public static double angleScoreSpecArm = .47 ;
    public static double angleScoreSpecClaw = 0.9;
    public static double angleScoreSample = 0.18;

    public static double angleWallSpecArm = .95;
    public static double angleWallSpecClaw = 0.15;

    public static double angleMiddleArm = .6;
    public static double angleMiddleClaw = 0.55;

    public static int wallPickupTarget = 0;
//    public static int transferPickupTarget = 0;

    public static int BucketTarget = 60000;
    public static int LowBucketTarget = 30000;
    public static int SpecimenTarget = 61500;

//    public static int TransferPickupTarget = 4800;
//    public static int TransferWaitTarget = 5000;

    public static int intermediateTarget = 0;
    public static int WallTarget = 0;

    public static int TransferTarget = 16000;

    public static int MaxExtension = 28000;
    public static int halfExtension= 14000;

    public static int TransferExtensionIn = 0;
    public static int TransferExtensionOut = 0;
    public static int MinExtension = 500;

    // LED Values
    public static double yellow = 0.388;
    public static double blue = 0.611;
    public static double red = 0.279;
    public static double green = 0.500;
    public static double off =0;

    public static double intakeintakearm = .503;
    public static double intakeintakechain = .08;

    public static int clawOpenWaitTime = 300;
    public static int clawCloseWaitTime= 400;

}

/*

   ╚ ╔ ╩ ╦ ╠ ═ ╬ ╣ ║ ╗ ╝
       THE WISDOM!
   T H E   W I S D O M !
                    ║
            ╔═══════╣
            ║  ╔════╣
            ║  ║    ║
            ║  ║    ║
                    ║

 */