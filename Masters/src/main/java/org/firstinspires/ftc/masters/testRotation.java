package org.firstinspires.ftc.masters;


import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Config
@Autonomous(name = "debugRotation")
public class testRotation extends LinearOpMode {
    private Follower follower;

    public enum State {Start, End};
    private State pathState;

    private PathChain scorePreload;


    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
      //  buildPaths();

        pathState = State.Start;
        waitForStart();


        while (opModeIsActive()){
            switch (pathState) {
                case Start:

                    follower.turnTo(Math.toRadians(45));
                    pathState = State.End;

                    break;
            }
            follower.update();
        }
    }

//    public void buildPaths() {
//        scorePreload= follower.pathBuilder()
//                .addPath(new BezierLine(startPose, scorePose))
//                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
//                .build();
//    }
}
