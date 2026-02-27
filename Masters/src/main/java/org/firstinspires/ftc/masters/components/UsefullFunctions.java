package org.firstinspires.ftc.masters.components;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class UsefullFunctions {
    public static boolean isInRange(double value, double min, double max) {
        return (value >= min && value <= max);
    }
    public static String getColor(RevColorSensorV3 sensor) {


        double distance = sensor.getDistance(DistanceUnit.MM);

        if (distance<20){
           // int argb = sensor.argb();
//            int red= (argb >>16) & 0xFF;
//            int green = (argb >>8) & 0xFF;
//            int blue = (argb & 0xFF);

            int blue = sensor.blue();
            int green = sensor.green();
            if (blue> green)
                return "purple";
            else if (green >=blue){
                return "green";
            }
        } else if (distance < 115){
//            int argb = sensor.argb();
//            int red= (argb >>16) & 0xFF;
//            int green = (argb >>8) & 0xFF;
//            int blue = (argb & 0xFF);

//            int red = sensor.red();
//            if (red>70){
//                return "purple";
//            } else
//                return "green";
            return "unknown";
        }

            return "nothing";
//        if (isInRange(green, Constant.minnothingColor, Constant.maxnothingColor)) {
//            return "nothing";
//        } else if (isInRange(green, Constant.minpurpleColor, Constant.maxpurpleColor)) {
//            return "purple";
//        } else if (isInRange(green, Constant.mingreenColor, Constant.maxgreenColor)) {
//            return "green";
//        }
//        return "nothing";
    }
}
