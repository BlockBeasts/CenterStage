package org.firstinspires.ftc.masters.vison.Confusion;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.masters.pedroPathing.Constants;

public class swapCords extends LinearOpMode {

    private Follower follower;

    private int pathState;

    // Sets out poses
    private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180));
    private final Pose scorePose = new Pose(60, 85, Math.toRadians(135));
    private final Pose movePose = new Pose(37, 121, Math.toRadians(0));

    // Inits our path chains
    private Path scorePreload;
    private PathChain moveChain;

    // Sets up blackboard
    public static final String POSE_KEY = "Pose";

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

            // Sets our current robot position to the blackboard while not moving
            if(!follower.isBusy()) {
                blackboard.put(POSE_KEY, follower.getPose());
            }

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
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if(!follower.isBusy()) {
                    follower.followPath(moveChain,true);
                    setPathState(2);
                }
                break;
            case 2:
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

        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        moveChain = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, movePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), movePose.getHeading())
                .build();
    }

}
