package org.firstinspires.ftc.masters;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.pedroPathing.Constants;

@Config // Enables FTC Dashboard
@Autonomous(name = "auto-redwall")

public class tankauto_redwall extends LinearOpMode {
    private Path scorePreload;

    private Follower follower;


    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotorImplEx shoot;
    CRServo pusher1;
    CRServo pusher2;
    int targetSpeed = 3000;
    double realSpeed = 0;


    private PathChain grabPickup1;
    private int pathState;
    private PathChain moveChain;

    private final Pose startPose = new Pose(28.5, 128, Math.toRadians(180));
    private final Pose scorePose = new Pose(60, 85, Math.toRadians(135));
    private final Pose movePose = new Pose(37, 121, Math.toRadians(0));
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public void runOpMode() throws InterruptedException {

        double speedTPS= (double) (28 * targetSpeed) / 60;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.update();


        shoot = hardwareMap.get(DcMotorImplEx.class, "shooter");
        pusher1 = hardwareMap.crservo.get("pusher1");
        pusher2 = hardwareMap.crservo.get("pusher2");

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();
        setPathState(0);
        shoot.setVelocity(speedTPS);

        while (opModeIsActive()) {
            follower.update();
            autonomousPathUpdate();

            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();

        }
    }



    public void shootBall(){
        while (realSpeed < targetSpeed && opModeIsActive()) {
            realSpeed = shoot.getVelocity();
        }
        pusher1.setPower(1);
        pusher2.setPower(1);
        ElapsedTime elapsedTime = new ElapsedTime();

        while(elapsedTime.milliseconds() < 2000 && opModeIsActive()) {
            pusher1.setPower(0);
            pusher2.setPower(0);
        }



    }
    public void buildPaths() {

        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        moveChain = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, movePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), movePose.getHeading())
                .build();
    }
    public void setPathState(int pState) {
        pathState = pState;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                shootBall();
                shootBall();
                shootBall();
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

}
