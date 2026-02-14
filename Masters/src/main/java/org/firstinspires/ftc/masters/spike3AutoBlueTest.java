package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Outake;
import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Config
@Autonomous(name = "goal auto blue test")

public class spike3AutoBlueTest extends LinearOpMode {

    Init init;
    Intake intake;
    Outake outake;


    private Follower follower;

    private final Pose startPose = new Pose(28.5, 128, Math.toRadians(145));
    private final Pose scorePose = new Pose(28.5, 128, Math.toRadians(135));
    private final Pose pickup1Pose = new Pose(55, 87, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose endPickup1 = new Pose (24, 87, Math.toRadians(180));
    private final Pose pickup2Pose = new Pose(55, 63.5, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose endPickup2 = new Pose(24, 63.5, Math.toRadians(180));
    private final Pose pickup3Pose = new Pose(55, 40, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose endPickup3 = new Pose(24, 40, Math.toRadians(180));

    private final Pose endPose = new Pose (60, 85, Math.toRadians(135)); // need to change values to get off the line

    private Path scorePreload;
    private PathChain spike1, pickup1, score1, spike2, pickup2, score2, spike3, pickup3, score3, end;

    public enum State {Start, ToGoal,ToSpike, Pickup, ToSpike1, ToSpike2, ToSpike3,End};
    private State pathState;

    int scored = 0;

    double run = 1;
    double pick = .3;

    ElapsedTime elapsedTime = new ElapsedTime();


    public void runOpMode() throws InterruptedException {

        init = new Init(hardwareMap);
        outake = new Outake(init);
        intake = new Intake(init);

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();

        pathState = State.Start;

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
            case Start:
                outake.down();
                if (outake.isInDownPos()) {
                    //follower.followPath(scorePreload);
                    pathState = State.ToGoal;
                }

                break;
            case ToGoal:
                if(!follower.isBusy()) {

                        outake.launch();

                    if (outake.isInUpPos()) {
                        if (scored==0) {
                            outake.down();
                            follower.followPath(spike1, run, false);
                            pathState = State.ToSpike;
                        } else if (scored ==1 ){
                            outake.down();
                            follower.followPath(spike2, run, false);
                            pathState = State.ToSpike;
                        } else if (scored ==2){
                            outake.down();
                            follower.followPath(spike3, run, false);
                            pathState = State.ToSpike;
                        } else {
                            follower.followPath(end);
                            pathState = State.End;
                        }

                    }

                } else {
                    if (elapsedTime!=null && elapsedTime.milliseconds()>3000){
                        intake.intakeOff();
                    }
                }



                break;
            case ToSpike:
                if(!follower.isBusy()) {
                    //pick up
                    intake.intakeOn();
                    if (scored == 0){
                        follower.followPath(pickup1, pick, false);
                    } else if (scored ==1){
                        follower.followPath(pickup2, pick-.02, false);
                    } else if (scored ==2 ){
                        follower.followPath(pickup3, pick-.02, false);
                    }
                    pathState= State.Pickup;

                }
                break;
            case Pickup:
                if(!follower.isBusy()) {
                   elapsedTime = new ElapsedTime();
                    if (scored == 0){
                        follower.followPath(score1, run, false);
                    } else if (scored ==1){
                        follower.followPath(score2, run, false);
                    } else if (scored ==2 ){
                        follower.followPath(score3, run, false);
                    }
                    scored++;
                    pathState = State.ToGoal;

                }
                break;
            case End:
                if (!follower.isBusy()){
                    intake.intakeOff();
                    outake.outakeUp();
                }
        }
    }

    public void buildPaths() {

        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        spike1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, pickup1Pose))
                .setLinearHeadingInterpolation(startPose.getHeading(), pickup1Pose.getHeading())
                .build();

        pickup1 = follower.pathBuilder()
                .addPath( new BezierLine(pickup1Pose, endPickup1))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), endPickup1.getHeading())
                .build();

        score1 = follower.pathBuilder()
                .addPath(new BezierLine(endPickup1, scorePose))
                .setLinearHeadingInterpolation(endPickup1.getHeading(), scorePose.getHeading())
                .build();

        spike2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath( new BezierLine(pickup2Pose, endPickup2))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), endPickup2.getHeading())
                .build();

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup2Pose, scorePose))
                .setLinearHeadingInterpolation(endPickup2.getHeading(), scorePose.getHeading())
                .build();

        spike3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath( new BezierLine(pickup3Pose, endPickup3))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), endPickup3.getHeading())
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(endPickup3.getHeading(), scorePose.getHeading())
                .build();

        end = follower.pathBuilder()
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(endPickup3.getHeading(), scorePose.getHeading())
                .build();
    }


}
