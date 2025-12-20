package org.firstinspires.ftc.masters.components;


//random math fuctions ;3

public class UsefullMath {
    public int angleToTicks(double angle, double ppr) {
        return (int) ((angle/360) * ppr);
    }
    public double ticksToAngle(double ticks, double ppr) {
        return (ticks/ppr) * 360;
    }
}
