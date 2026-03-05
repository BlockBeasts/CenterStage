package org.firstinspires.ftc.masters.components;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class UsefullFunctions {
    public static boolean isInRange(double value, double min, double max) {
        return (value >= min && value <= max);
    }
    public static String getColor(RevColorSensorV3 sensor, String position) {


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
        } else if (distance < getDistance(position)){

            return "unknown";
        }

            return "nothing";

    }

    public static int getDistance (String position){
        if ("left".equals(position)){
            return 115;
        } else if ("middle".equals(position)){
            return 80;
        } else {
            return 85;
        }
    }
}
