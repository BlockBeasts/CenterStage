package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

@Config
public class Constant {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double hoodDown = 0;
    public static double hoodMax = 0.5;

    public static double leftTrayBottom = 0.95;
    public static double middleTrayBottom = 0.95;
    public static double rightTrayBottom = 0;
    public static double leftTrayTop = 0.65;
    public static double middleTrayTop = 0.65;
    public static double rightTrayTop = 0.35;

    public static int shooterMin = 1500;
    public static int shooterMax= 2500;

    public static double greenLed = 0.5;
    public static double purpleLed = 0.722;

    public static double m=2.188;
    public static double b = 1391.5;

    public static double blueGoalX= 11.5;
    public static double goalY= 129.5;

    public static double redGoalX= 144-11.5;



}
