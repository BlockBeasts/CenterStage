package org.firstinspires.ftc.masters.components;


//random math fuctions ;3

public  class UsefullMath {
    public static int angleToTicks(double angle, double ppr) {
        return (int) ((angle/360) * ppr);
    }
    public static double ticksToAngle(double ticks, double ppr) {
        return (ticks/ppr) * 360;
    }
}
