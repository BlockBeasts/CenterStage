package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.masters.components.ITDCons;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.components.UsefullMath;
import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Config
@Autonomous(name = "protoBot auto")

public class spike3Auto extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;


    private Follower follower;

    private int pathState;



    private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180));
    private final Pose scorePose = new Pose(60, 85, Math.toRadians(135));
    private final Pose pickup1Pose = new Pose(37, 121, Math.toRadians(0)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose pickup2Pose = new Pose(43, 130, Math.toRadians(0)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0)); // Lowest (Third Set) of Artifacts from the Spike Mark.

    private Path scorePreload;
    private PathChain spike1, score1, spike2, score2, spike3, score3;


    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init);
        intake = new Intake(init);

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();

        setPathState(0);

        while (opModeIsActive()){

            // These loop the movements of the robot, these must be called continuously in order to work
            follower.update();
            autonomousPathUpdate();

            // Feedback to Driver Hub for debugging
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();

            outake.update(telemetry);


        }

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                outake.reset();
                if (outake.isInResetPos()) {
                    outake.outakeHold();
                    follower.followPath(scorePreload);
                    setPathState(1);
                }

                break;
            case 1:
                if(!follower.isBusy()) {
                    outake.launch();

                    if (outake.isInUpPos()) {
                        follower.followPath(spike1,true);

                        setPathState(2);
                    }



                }
                break;
            case 2:
                if(!follower.isBusy()) {


                    follower.followPath(score1,true);
                    setPathState(3);

                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    follower.followPath(spike2,true);
                    setPathState(4);
                    intake.intakeOn();
                }
                break;
            case 4:
                if(!follower.isBusy()) {
                    follower.followPath(score2,true);
                    setPathState(5);
                    intake.intakeOff();
                    outake.launch();
                }
                break;
            case 5:
                if(!follower.isBusy()) {
                    follower.followPath(spike3,true);
                    setPathState(6);
                    intake.intakeOn();
                }
                break;
            case 6:
                if(!follower.isBusy()) {
                    follower.followPath(score3,true);
                    setPathState(7);
                    intake.intakeOff();
                    outake.launch();
                }
                break;
            case 7:
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

        spike1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        spike2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, scorePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        spike3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
    }


}
