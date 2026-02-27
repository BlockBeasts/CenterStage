package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

@Config
public class Constant {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double hoodDown = 0;
    public static double hoodFar = 0.15;

    public static double leftTrayBottom = 0.95;
    public static double middleTrayBottom = 0.95;
    public static double rightTrayBottom = 0;
    public static double leftTrayTop = 0.6;
    public static double middleTrayTop = 0.6;
    public static double rightTrayTop = 0.35;

    public static int shooterMin = 1440;
    public static int shooterFar = 1830;

    public static double minnothingColor = 0;
    public static double maxnothingColor = 400;
    public static double mingreenColor = 600;
    public static double maxgreenColor = 1750;
    public static double minpurpleColor = 1800;
    public static double maxpurpleColor = 2000;

    public static double greenLed = 0.5;
    public static double purpleLed = 0.722;

    public static double orangeLed = 0.333;

    public static double m=2.188;
    public static double b = 1391.5;

    public static double blueGoalX= 11.5;
    public static double goalY= 129.5;

    public static double redGoalX= 144-11.5;

    public enum AllianceColor {
        RED, BLUE
    }






}
