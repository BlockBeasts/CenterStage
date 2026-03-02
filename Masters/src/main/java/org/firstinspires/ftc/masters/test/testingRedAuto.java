package org.firstinspires.ftc.masters.test;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Autonomous(name="desperate test")
public class testingRedAuto extends LinearOpMode {

    Follower follower;
    private final Pose startPose = new Pose(127.8, 112.6, Math.toRadians(127));

    private final Pose tagPose = new Pose(100, 110, Math.toRadians(90));

    private final Pose scorePose = new Pose(89, 108, Math.toRadians(180 - 135));

    PathChain readTag, scorePreload;

    @Override
    public void runOpMode() throws InterruptedException {

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        waitForStart();

        follower.followPath(readTag);

        while (opModeIsActive()) {
            follower.update();
        }
    }

    public void buildPaths() {

        readTag = follower.pathBuilder()
                .addPath(new BezierLine(startPose, tagPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), tagPose.getHeading())
                .addPath(new BezierLine(tagPose, scorePose))
                .setLinearHeadingInterpolation(tagPose.getHeading(), scorePose.getHeading())
                .build();

        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(tagPose, scorePose))
                .setLinearHeadingInterpolation(tagPose.getHeading(), scorePose.getHeading())
                .build();

    }
}
