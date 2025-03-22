package org.firstinspires.ftc.masters.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.GoBildaPinpointDriver;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
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

@Autonomous(name="SpecimenPush")
public class SpecimenPush5 extends LinearOpMode {

    Pose startPose = new Pose(10,45,0);
    Pose scoringPose = new Pose(40,70.5, 0);

    Pose midPoint2 = new Pose(60,36,0);

    Pose pickupPose = new Pose (13.5,38.5, 0);
    Pose pickupMid = new Pose(34,38,0);
    Pose pushPose1 = new Pose(64,28,0);
    Pose endPushPose1 = new Pose (29,28,0);
    Pose pushPose2 = new Pose(64,17,0);
    Pose endPushPose2 = new Pose(29,17,0);
    Pose pushPose3 = new Pose(64,10,0);
    Pose endPushPose3 = new Pose(29,10,0);
    Pose park = new Pose(20,25,Math.toRadians(90));

    Path scorePreload, pickup1, score, towall, tosub, end;
    PathChain pushSample1, pushSample2, pushSample3, pickUp;

    int cycleCount = 1;

    enum PathState {Lift,Start,ToSub, ScorePreload,Sample1,PushSample1,  PickUpSpec, GrabSpec3, ScoreSpec, Score, End}

    Follower follower;

    GoBildaPinpointDriver pinpoint;
    Servo led;

    @Override
    public void runOpMode() throws InterruptedException {

        ElapsedTime elapsedTime = null;
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        Init init = new Init(hardwareMap);
        Outtake outtake = new Outtake(init, telemetry);
        Intake intake = new Intake(init, telemetry);
        DriveTrain driveTrain = new DriveTrain(init, telemetry);

        pinpoint = init.getPinpoint();
        led = init.getLed();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        PathState state = PathState.Start;
        //outtake.initAutoSpecimen();
        intake.retractSlide();

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

        outtake.wrist.setPosition(ITDCons.wristBack);
        outtake.setAngleServoToBack();

        //outtake.scoreSpecimen();
        intake.servoToTransfer();

        elapsedTime = new ElapsedTime();
        follower.followPath(pushSample1);

        int scoreCount=0;

        while (opModeIsActive() && !isStopRequested()) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }

            switch (state){
                case Start:
                    if (!follower.isBusy()){

                        state = PathState.PickUpSpec;
                        elapsedTime= null;
                       // follower.followPath(pickup1);

                    } else if (elapsedTime.milliseconds()>5000){
                        outtake.moveToPickUpFromWall();

                    }
                    break;
                case PickUpSpec:
                    if(!follower.isBusy()) {
                        if (elapsedTime == null) {
                            outtake.closeClaw();
                            elapsedTime = new ElapsedTime();
                        }
                        if (elapsedTime != null && elapsedTime.milliseconds() > ITDCons.clawCloseWaitTime) {
                            follower.followPath(score);
                            outtake.scoreSpecimen();
                            state = PathState.ScoreSpec;
                            elapsedTime = null;
                        }
                    }
                    break;
                case ScoreSpec:
                    if (!follower.isBusy()){
                        if (elapsedTime==null) {
                            outtake.openClaw();
                            scoreCount++;
                            elapsedTime= new ElapsedTime();
                        }
                        if (elapsedTime.milliseconds()>ITDCons.clawOpenWaitTime){
                            if (scoreCount<5) {
                                follower.followPath(towall);
                                elapsedTime = null;
                                state = PathState.PickUpSpec;
                            } else {
                                follower.followPath(end);

                                state = PathState.End;
                            }
                        }
                    }
                    break;

//                case ToSub:
//                    if (elapsedTime!=null && elapsedTime.milliseconds()>0){
//                        driveTrain.drive(0);
//                        outtake.openClawAuto();
//                        state = PathState.ScorePreload;
//                        elapsedTime = new ElapsedTime();
//                    }
//                    break;
//                case ScorePreload:
//                    if (elapsedTime!=null && elapsedTime.milliseconds()>100){
//                        follower.followPath(pushSample1);
//                        outtake.closeClaw();
//                        outtake.moveToPickUpFromWall();
//                        state = PathState.Sample1;
//                        elapsedTime = null;
//                    }
//                    break;
//                case Sample1:
//                    if (!follower.isBusy()){
//
//
//                        follower.followPath(pushSample2);
//                        state= PathState.Sample2;
//                    }
//                    break;
//                case Sample2:
//                    if (!follower.isBusy()){
////                        outtake.moveToPickUpFromWall();
////                        follower.followPath(pushSample3);
//                        state= PathState.Sample3;
//                    }
//                    break;
//                case Sample3:
//                    if (!follower.isBusy()){
//                        outtake.openClaw();
//                        follower.followPath(pickup1);
//                        state= PathState.PickUpSpec;
//                        elapsedTime = null;
//                    }
//                    break;
//                case PickUpSpec:
//                    if (!follower.isBusy()){
//                        if (elapsedTime==null) {
//                            outtake.closeClaw();
//                            elapsedTime= new ElapsedTime();
//
//                        } else if (elapsedTime.milliseconds()>500){
//                            follower.followPath(score);
//                            outtake.scoreSpecimen();
//                            elapsedTime=null;
//                            if(cycleCount <= 4) {
//                                state = PathState.ScoreSpec3;
//                            }
//                            if(cycleCount > 4) {
//                                state = PathState.Score;
//                            }
//
//                        }
//
//                    }
//                    break;
//                case ScoreSpec3:
//                    if (!follower.isBusy()){
//                        if (elapsedTime==null) {
//                            outtake.openClawAuto();
//                            cycleCount++;
//                            elapsedTime= new ElapsedTime();
//
//                        } else if (elapsedTime.milliseconds()>350){
//                            follower.followPath(towall);
//                            outtake.closeClaw();
//                            outtake.moveToPickUpFromWall();
//                            elapsedTime = null;
//                            state = PathState.PickUpSpec;
//
//                        }
//                    }
//                    break;
//
////                case Score:
////                    if (!follower.isBusy()){
////                        if (elapsedTime==null) {
////                            //outtake.openClaw();
////                            elapsedTime= new ElapsedTime();
////
////                        } else if (elapsedTime.milliseconds()>150){
////                            follower.followPath(pickUp);
////                            //outtake.moveToPickUpFromWall();
////                            elapsedTime=null;
////                            state= PathState.End;
////                        }
////                    }
////                    break;
////                case End:
////                    if (!follower.isBusy()){
////                        //outtake.setTarget(0);
////                    }
////                    break;
            }


            outtake.update();
            intake.update();
            follower.update();
        }
    }

    protected void buildPaths(){

      //  scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scoringPose)));
      //  scorePreload.setConstantHeadingInterpolation(startPose.getHeading());

        pushSample1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose),new Point(midPoint2), new Point(pushPose1)))
                .setLinearHeadingInterpolation(startPose.getHeading(), pushPose1.getHeading())
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

                .build();


        pickup1 = new Path(new BezierCurve(new Point(endPushPose3), new Point(pickupMid), new Point(pickupPose)));
        pickup1.setLinearHeadingInterpolation(endPushPose3.getHeading(), pickupPose.getHeading());

        score = new Path(new BezierLine(new Point(pickupPose), new Point(scoringPose.getX(), scoringPose.getY())));
        score.setLinearHeadingInterpolation(pickupPose.getHeading(), scoringPose.getHeading());

        towall = new Path(new BezierCurve(new Point(scoringPose.getX(), scoringPose.getY()), new Point(pickupPose)));
        towall.setLinearHeadingInterpolation(scoringPose.getHeading(), pickupPose.getHeading());

        end = new Path(new BezierCurve(new Point(scoringPose), new Point(park)));
        end.setLinearHeadingInterpolation(scoringPose.getHeading(), park.getHeading());

//        pickUp = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(scoringPose), new Point(pickupPose)))
//                .setLinearHeadingInterpolation(scoringPose.getHeading(), pickupPose.getHeading())
//                .build();




    }
}
