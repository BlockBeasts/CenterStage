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

    public enum State {Start, Turn, End};
    private State pathState;

    private PathChain scorePreload;

    private final Pose startPose = new Pose(144-56, 8.8, Math.toRadians(90));
    private final Pose scorePose = new Pose(88.6, 17.4, Math.toRadians(67.9));


    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        pathState = State.Start;
        waitForStart();

        follower.followPath(scorePreload);
        while (opModeIsActive()){
            switch (pathState) {
                case Start:

                    if (!follower.isBusy()) {

                        follower.turnTo(Math.toRadians(55));
                        pathState = State.Turn;
                    }

                    break;

                case Turn:


                    pathState = State.End;
                    break;


            }


            follower.update();
        }
    }

    public void buildPaths() {
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
    }
}
