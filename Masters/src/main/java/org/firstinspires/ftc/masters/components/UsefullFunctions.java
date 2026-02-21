package org.firstinspires.ftc.masters.components;

public class UsefullFunctions {
    public static boolean isInRange(double value, double min, double max) {
        return (value >= min && value <= max);
    }
    public static String getColor(double green) {
        if (isInRange(green, Constant.minnothingColor, Constant.maxnothingColor)) {
            return "nothing";
        } else if (isInRange(green, Constant.minpurpleColor, Constant.maxpurpleColor)) {
            return "green";
        } else if (isInRange(green, Constant.mingreenColor, Constant.maxgreenColor)) {
            return "purple";
        }
        return "nothing";
    }
}
