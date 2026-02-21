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
    public static double leftTrayTop = 0.6;
    public static double middleTrayTop = 0.6;
    public static double rightTrayTop = 0.35;

    public static int shooterMin = 1500;
    public static int shooterMax= 2500;

    public static double minnothingColor = 0;
    public static double maxnothingColor = 400;
    public static double mingreenColor = 600;
    public static double maxgreenColor = 1750;
    public static double minpurpleColor = 1800;
    public static double maxpurpleColor = 2000;

    public static double greenLed = 0.5;
    public static double purpleLed = 0.722;





}
