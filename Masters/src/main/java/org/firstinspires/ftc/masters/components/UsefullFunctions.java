package org.firstinspires.ftc.masters.components;

public class UsefullFunctions {
    public static boolean isInRange(double value, double min, double max) {
        return (value >= min && value <= max);
    }
    public static String getColor(double green) {
        if (green <= Constant.nothingColor) {
            return "nothing";
        } else if (isInRange(green, Constant.greenColor, Constant.purpleColor)) {
            return "green";
        } else if (green <= Constant.purpleColor + 400) {
            return "purple";
        }
        return "nothing";
    }
}
