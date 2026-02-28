package org.firstinspires.ftc.masters;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Autonomous(name = "Auto Wall Red")
public class JustATapRight extends LinearOpMode {

    private Follower follower;

    private int pathState;

    public int moveDistance = 24;

    private final Pose startPose = new Pose(72, 72, Math.toRadians(90));
    private final Pose stopPose = new Pose(72+moveDistance, 72+3, Math.toRadians(90));
    private PathChain moveOut;


    public static final String POSE_KEY_X = "PoseX";
    public static final String POSE_KEY_Y = "PoseY";
    public static final String POSE_KEY_H = "PoseH";
    public void runOpMode() throws InterruptedException {

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();

        setPathState(0);

        while (opModeIsActive()){

            // These loop the movements of the robot, these must be called continuously in order to work
            follower.update();
            autonomousPathUpdate();

            blackboard.put(POSE_KEY_X, follower.getPose().getX());
            blackboard.put(POSE_KEY_Y, follower.getPose().getY());
            blackboard.put(POSE_KEY_H, follower.getPose().getHeading());
            telemetry.addData("saved pos x", blackboard.get(POSE_KEY_X));
            telemetry.addData("saved pos y", blackboard.get(POSE_KEY_Y));
            telemetry.addData("saved pos h", blackboard.get(POSE_KEY_H));


            // Feedback to Driver Hub for debugging
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();

        }

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if(!follower.isBusy()) {
                    follower.followPath(moveOut,true);
                    setPathState(1);
                }
                break;
            case 1:
                if(!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
    }

    public void buildPaths() {

        moveOut = follower.pathBuilder()
                .addPath(new BezierLine(startPose, stopPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), stopPose.getHeading())
                .build();

    }

}
