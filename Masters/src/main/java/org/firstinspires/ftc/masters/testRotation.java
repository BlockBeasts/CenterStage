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

    private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180));
    private final Pose scorePose = new Pose(60, 85, Math.toRadians(135));
    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
      //  buildPaths();

        pathState = State.Start;
        waitForStart();


        while (opModeIsActive()){
            switch (pathState) {
                case Start:
                    follower.followPath(scorePreload);

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
