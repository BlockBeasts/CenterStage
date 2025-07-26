package org.firstinspires.ftc.masters.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.GoBildaPinpointDriver;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.components.DriveTrain;
import org.firstinspires.ftc.masters.components.ITDCons;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Outtake;
import org.firstinspires.ftc.masters.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.masters.pedroPathing.constants.LConstants;

import java.util.List;

//position are setup with pedro coordinate from blue side
//auto can be used for blue and red

@Autonomous(name="Specimen canada")
public class specimenCanada extends LinearOpMode {

    public static double p = 0.0003, i = 0, d =.0000001;


    Pose startPose = new Pose(10,66,0);
    Pose scoringPose = new Pose(42,70.5, 0);

    Pose midPoint0 = new Pose(30, 60, 0);
    Pose midPoint1 = new Pose(20,25,0);
    Pose midPoint2 = new Pose(60,36,0);

    Pose pickupPose1 = new Pose(17, 35, 0);
    Pose pickupPose2 = new Pose(17, 32, 0);
    Pose pickupPose3 = new Pose(17, 32, 0);
    Pose pickupPose4 = new Pose(17, 32, 0);
    Pose pickupPose = new Pose (17.5,38, 0);
    Pose pushPose1 = new Pose(60,28,0);
    Pose endPushPose1 = new Pose (35,28,0);
    Pose pushPose2 = new Pose(60,17,0);
    Pose endPushPose2 = new Pose(35,17,0);
    Pose pushPose3 = new Pose(60,10,0);
    Pose endPushPose3 = new Pose(35,10,0);
    Pose pickupMid = new Pose(30,38,0);
    Pose pickupCycleMid = new Pose(30, 38, 0);
    Pose pickupCycleMid1 = new Pose(30,65, 0);

    Path scorePreload, pickup1, score, tosub, pickup;
    PathChain pushSample1, pushSample2, pushSample3, towall, toWall2, toWall3, toWall4;

    int cycleCount = 1;

    enum PathState {Lift,Start,ToSub, ScorePreload,Sample1,PushSample1, Sample2, PushSample2,
        Sample3, PushSample3, PickUpSpec, GrabSpec3, ScoreSpec3, Score, End, PickupSpec4, ScoreSpec4, PickupSpec5, ScoreSpec5,
    PickupSpec6, ScoreSpec6}

    Follower follower;
    GoBildaPinpointDriver pinpoint;
    Servo led;

    @Override
    public void runOpMode() throws InterruptedException {

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        ElapsedTime elapsedTime = null;

        Init init = new Init(hardwareMap);
        Outtake outtake = new Outtake(init, telemetry);
        Intake intake = new Intake(init, telemetry);
        DriveTrain driveTrain = new DriveTrain(init, telemetry);

        outtake.initializeHardware(p,i,d);

        pinpoint = init.getPinpoint();
        led = init.getLed();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        PathState state = PathState.Lift;
        outtake.setSpecAutoInit();
        intake.retractSlide();
        outtake.closeClaw();
        intake.pushIn();

        pinpoint.update();
        telemetry.addData("Pinpoint Status", pinpoint.getDeviceStatus());
        telemetry.update();

        while (pinpoint.getDeviceStatus() != GoBildaPinpointDriver.DeviceStatus.READY){
            pinpoint.update();
            telemetry.addData("Pinpoint Status", pinpoint.getDeviceStatus());
            telemetry.update();
            led.setPosition(ITDCons.red);
            sleep(500);
        } if (pinpoint.getDeviceStatus() == GoBildaPinpointDriver.DeviceStatus.READY){
            pinpoint.update();
            telemetry.addData("Pinpoint Status", pinpoint.getDeviceStatus());
            telemetry.update();
            led.setPosition(ITDCons.green);
        }

        waitForStart();

        outtake.scoreSpecimen();
        ElapsedTime time = new ElapsedTime();
        while (opModeIsActive() && time.milliseconds()<300){
            outtake.update();
        }
        follower.followPath(scorePreload);

        elapsedTime = new ElapsedTime();


        while (opModeIsActive() && !isStopRequested()) {

            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }

            switch (state){
                case Lift:
                    if (elapsedTime.milliseconds()>200){

                        intake.servoToNeutral();
                        elapsedTime= new ElapsedTime();
                        state= PathState.Start;
                    }
                    break;
                case Start:
                    if (!follower.isBusy() || elapsedTime.milliseconds()>1500){

                        elapsedTime = new ElapsedTime();
                        state = PathState.ToSub;
                        driveTrain.drive(1);

                    }
                    break;
                case ToSub:
                    if (elapsedTime!=null && elapsedTime.milliseconds()>0){
                        driveTrain.drive(0);
                        outtake.openClawAuto();
                        state = PathState.ScorePreload;
                        follower.followPath(pushSample1,true);
                        elapsedTime = new ElapsedTime();
                    }
                    break;
                case ScorePreload:
                    if (elapsedTime!=null && elapsedTime.milliseconds()>350){

                        outtake.moveToPickUpFromWall();
                        state = PathState.Sample3;
                        elapsedTime = new ElapsedTime();
                    }
                    break;

                case Sample3:
                    if (!follower.isBusy()){

                        state= PathState.PickUpSpec;
                        outtake.closeClaw();
                        elapsedTime= new ElapsedTime();
                    }
                    if (elapsedTime!=null && elapsedTime.milliseconds()>5000){
                        outtake.openClaw();
                    }
                    break;
                case PickUpSpec:
                    if (!follower.isBusy()){
                        if (elapsedTime==null){
                            elapsedTime = new ElapsedTime();
                        }
                        if (elapsedTime!=null && elapsedTime.milliseconds()>200){
                            outtake.closeClaw();
                        }
                      if (elapsedTime!=null && elapsedTime.milliseconds()>350){
                            follower.followPath(score);
                            outtake.scoreSpecimen();
                            elapsedTime=null;
                            if(cycleCount <= 4) {
                                state = PathState.ScoreSpec3;
                            }
                            if(cycleCount > 4) {
                                state = PathState.Score;
                            }
                        }
                    }
                    break;
                case ScoreSpec3:
                    if (!follower.isBusy()){
                        if (elapsedTime==null) {
                            outtake.openClawAuto();
                            cycleCount++;
                            elapsedTime= new ElapsedTime();

                        } else if (elapsedTime.milliseconds()>150){
                            follower.followPath(towall);
                            outtake.moveToPickUpFromWall();
                            elapsedTime = null;
                            if (cycleCount<2) {
                                state = PathState.PickUpSpec;
                            } else
                                state = PathState.PickupSpec4;
                        }
                    }
                    break;
                case PickupSpec4:
                    if (!follower.isBusy()){
                        if (elapsedTime==null){
                            elapsedTime = new ElapsedTime();
                        }
                        if (elapsedTime.milliseconds()>200){
                            outtake.closeClaw();
                        }
                        if (elapsedTime.milliseconds()>350){
                            follower.followPath(score);
                            outtake.scoreSpecimen();
                            elapsedTime=null;
                            if(cycleCount <= 5) {
                                state = PathState.ScoreSpec4;
                            }
                            if(cycleCount > 5) {
                                state = PathState.Score;
                            }
                        }
                    }
                    break;
                case ScoreSpec4:
                    if (!follower.isBusy()){
                        if (elapsedTime==null) {
                            outtake.openClawAuto();
                            cycleCount++;
                            elapsedTime= new ElapsedTime();

                        } else if (elapsedTime.milliseconds()>150){
                            follower.followPath(toWall2);
                            outtake.moveToPickUpFromWall();
                            elapsedTime = null;
                            if (cycleCount<=5) {
                                state = PathState.PickupSpec5;
                            } else
                                state = PathState.Score;
                        }
                    }
                    break;
                case PickupSpec5:
                    if (!follower.isBusy()){
                        if (elapsedTime==null){
                            elapsedTime = new ElapsedTime();
                        }
                        if (elapsedTime.milliseconds()>200){
                            outtake.closeClaw();
                        }
                        if (elapsedTime.milliseconds()>350){
                            follower.followPath(score);
                            outtake.scoreSpecimen();
                            elapsedTime=null;
                            if(cycleCount <= 5) {
                                state = PathState.ScoreSpec5;
                            }
                            if(cycleCount > 5) {
                                state = PathState.Score;
                            }
                        }
                    }
                    break;
                case ScoreSpec5:
                    if (!follower.isBusy()){
                        if (elapsedTime==null) {
                            outtake.openClawAuto();
                            cycleCount++;
                            elapsedTime= new ElapsedTime();

                        } else if (elapsedTime.milliseconds()>150){
                            follower.followPath(toWall3);
                            outtake.moveToPickUpFromWall();
                            elapsedTime = null;
                            if (cycleCount<=5) {
                                state = PathState.PickupSpec6;
                            } else
                                state = PathState.Score;
                        }
                    }
                    break;
                case PickupSpec6:
                    if (!follower.isBusy()){
                        if (elapsedTime==null){
                            elapsedTime = new ElapsedTime();
                        }
                        if (elapsedTime.milliseconds()>200){
                            outtake.closeClaw();
                        }
                        if (elapsedTime.milliseconds()>350){
                            follower.followPath(score);
                            outtake.scoreSpecimen();
                            elapsedTime=null;
                            if(cycleCount <= 5) {
                                state = PathState.ScoreSpec6;
                            }
                            if(cycleCount > 5) {
                                state = PathState.Score;
                            }
                        }
                    }
                    break;
                case ScoreSpec6:
                    if (!follower.isBusy()){
                        if (elapsedTime==null) {
                            outtake.openClawAuto();
                            cycleCount++;
                            elapsedTime= new ElapsedTime();

                        } else if (elapsedTime.milliseconds()>150){
                            follower.followPath(toWall4);
                            outtake.moveToPickUpFromWall();
                            elapsedTime = null;
                            if (cycleCount<=5) {
                                state = PathState.PickupSpec5;
                            } else
                                state = PathState.Score;
                        }
                    }
                    break;
            }

            outtake.update();
            intake.update();
            follower.update();

            telemetry.addData("count", cycleCount);
            telemetry.update();
        }
    }

    protected void buildPaths(){

        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scoringPose)));
        scorePreload.setConstantHeadingInterpolation(startPose.getHeading());

        pushSample1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoringPose),new Point(midPoint0), new Point(midPoint1), new Point(midPoint2), new Point(pushPose1)))
                .setLinearHeadingInterpolation(scoringPose.getHeading(), pushPose1.getHeading())
                .addPath(new BezierLine(new Point(pushPose1),new Point(endPushPose1)))
                .setLinearHeadingInterpolation(pushPose1.getHeading(), endPushPose1.getHeading())
                .addPath(new BezierCurve(new Point(endPushPose1),new Point(pushPose1),new Point (pushPose2)))
                .setLinearHeadingInterpolation(endPushPose1.getHeading(), pushPose2.getHeading())
                .addPath(new BezierLine(new Point(pushPose2), new Point(endPushPose2)))
                .setLinearHeadingInterpolation(pushPose2.getHeading(), endPushPose2.getHeading())
                .addPath(new BezierCurve(new Point(endPushPose2),new Point(pushPose2),new Point (pushPose3)))
                .setLinearHeadingInterpolation(endPushPose2.getHeading(), pushPose3.getHeading())
                .addPath(new BezierLine(new Point(pushPose3), new Point(endPushPose3)))
                .setLinearHeadingInterpolation(pushPose3.getHeading(), endPushPose3.getHeading())
                .addPath(new BezierCurve(new Point(endPushPose3), new Point(pickupMid), new Point(pickupPose)))
                .setLinearHeadingInterpolation(endPushPose3.getHeading(), pickupPose.getHeading())
                .setConstantHeadingInterpolation(0)
                .setPathEndVelocityConstraint(0.04)
                .setPathEndTimeoutConstraint(0)
                .build();

        score = new Path(new BezierLine(new Point(pickupPose), new Point(scoringPose.getX(), scoringPose.getY()-2)));
        score.setLinearHeadingInterpolation(pickupPose.getHeading(), scoringPose.getHeading());

        towall = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose.getX(),scoringPose.getY()-2), new Point(pickupCycleMid1)))
                .setConstantHeadingInterpolation(0)
                .addPath(new BezierCurve(new Point(pickupCycleMid1), new Point(pickupCycleMid), new Point(pickupPose1)))
                .setConstantHeadingInterpolation(0)
                .setPathEndVelocityConstraint(0.04)
                .setPathEndTimeoutConstraint(0)
                .build();

        toWall2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose.getX(),scoringPose.getY()-2), new Point(pickupCycleMid1)))
                .setConstantHeadingInterpolation(0)
                .addPath(new BezierCurve(new Point(pickupCycleMid1), new Point(pickupCycleMid), new Point(pickupPose2)))
                .setConstantHeadingInterpolation(0)
                .setPathEndVelocityConstraint(0.04)
                .setPathEndTimeoutConstraint(0)
                .build();

        toWall3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose.getX(),scoringPose.getY()-2), new Point(pickupCycleMid1)))
                .setConstantHeadingInterpolation(0)
                .addPath(new BezierCurve(new Point(pickupCycleMid1), new Point(pickupCycleMid), new Point(pickupPose3)))
                .setConstantHeadingInterpolation(0)
                .setPathEndVelocityConstraint(0.04)
                .setPathEndTimeoutConstraint(0)
                .build();

        toWall4 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose.getX(),scoringPose.getY()-2), new Point(pickupCycleMid1)))
                .setConstantHeadingInterpolation(0)
                .addPath(new BezierCurve(new Point(pickupCycleMid1), new Point(pickupCycleMid), new Point(pickupPose4)))
                .setConstantHeadingInterpolation(0)
                .setPathEndVelocityConstraint(0.04)
                .setPathEndTimeoutConstraint(0)
                .build();

//        towall = new Path(new BezierCurve(new Point(scoringPose.getX(), scoringPose.getY()+2), new Point(pickupCycleMid), new Point(pickupCycleMid1),  new Point(pickupPose1)));
//        towall.setTangentHeadingInterpolation();

//        pickUp = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(scoringPose), new Point(pickupPose1), new Point(pickupPose)))
//                .setLinearHeadingInterpolation(scoringPose.getHeading(), pickupPose.getHeading())
//                .build();




    }
}
