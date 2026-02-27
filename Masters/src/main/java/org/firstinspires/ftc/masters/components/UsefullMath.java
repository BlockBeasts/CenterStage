package org.firstinspires.ftc.masters.components;


//random math fuctions ;3

import com.pedropathing.geometry.Pose;

public  class UsefullMath {
    public static int angleToTicks(double angle, double ppr) {
        return (int) ((angle/360) * ppr);
    }
    public static double ticksToAngle(double ticks, double ppr) {
        return (ticks/ppr) * 360;
    }

    public static int getVelocityBlue (Pose pose2D){
        double x, y;

        x= pose2D.getX();
        y = pose2D.getY();

        int velocity =0;
        if (y <40){
            velocity = Constant.shooterFar;
        } else {
            double distance = Math.sqrt(((x - Constant.blueGoalX) * (x - Constant.blueGoalX)) + ((y - Constant.goalY) * (y - Constant.goalY)));
            if (distance<=32){
                velocity = Constant.shooterMin;
            } else {
                velocity = (int) (Constant.m * distance + Constant.b);
            }
        }

        return velocity;
    }

    public static int getVelocityRed (Pose pose){
        double x, y;
        x= pose.getX();
        y = pose.getY();
        int velocity =0;
        if (y <40){
            velocity = Constant.shooterFar;
        } else {
            double distance = Math.sqrt(((x - Constant.redGoalX) * (x - Constant.redGoalX)) + ((y - Constant.goalY) * (y - Constant.goalY)));

            if (distance <= 32) {
                velocity = Constant.shooterMin;
            } else {
                velocity = (int) (Constant.m * distance + Constant.b);
            }
        }

        return velocity;
    }
}
