package org.firstinspires.ftc.masters.autonomous;

import com.acmerobotics.dashboard.config.Config;
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
import org.firstinspires.ftc.masters.components.Hang;
import org.firstinspires.ftc.masters.components.ITDCons;
import org.firstinspires.ftc.masters.components.Init;
import org.firstinspires.ftc.masters.components.Intake;
import org.firstinspires.ftc.masters.components.Outtake;
import org.firstinspires.ftc.masters.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.masters.pedroPathing.constants.LConstants;

import java.util.List;

@Config
@Autonomous(name="Sample")
public class Sample extends LinearOpMode {

    public static double p = 0.0003, i = 0, d =.0000001;

    Pose startPose = new Pose(10.5,109.5,-90);
    Pose bucketPose = new Pose (25.5,136.5, Math.toRadians(-50));

    Pose bucketPose1 = new Pose (25.5,139.5, Math.toRadians(-50));
    Pose sample1 = new Pose(29.5,133.5,Math.toRadians(-50));

    Pose midPoint = new Pose(21,120, Math.toRadians(-45));
    Pose sample2 = new Pose(19.5,133.5,Math.toRadians(-12));
    Pose sample3 = new Pose(16, 121.5, Math.toRadians(30));

    Pose park = new Pose(85, 50, Math.toRadians(0));

    PathChain scorePreload, pickupSample1, pickupSample2, pickupSample3, scoreSample1_1, scoreSample1_2, scoreSample2, scoreSample3, parker;

    Follower follower;
    GoBildaPinpointDriver pinpoint;
    Servo led;

    enum PathState {ToBucket, Sample1, Sample2, Sample3, Score1, Score2, Score3, Score1Lift, Score2Lift, Score3LIft,StartLift, Park}

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
        Hang hang = new Hang(init, telemetry);
        outtake.setDriveTrain(driveTrain);
        outtake.setIntake(intake);
        intake.setOuttake(outtake);
        outtake.initializeHardware(p,i,d);
        outtake.setIsAuto(true);

        pinpoint = init.getPinpoint();
        led = init.getLed();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        outtake.initAutoSample();
        outtake.setAngleServoAutoInit();

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

        int count =0;

        waitForStart();

        intake.pushIn();
        intake.servoToNeutral();

        elapsedTime = new ElapsedTime();
        ElapsedTime  elapsedTimeFollow =null;
        PathState pathState =PathState.StartLift;
        outtake.scoreSample();


        while (opModeIsActive() && !isStopRequested()) {

            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }

            switch (pathState){
                case StartLift:
                    if (elapsedTime.milliseconds() > 500){
                        follower.followPath(scorePreload);
                        elapsedTime = null;
                        pathState = PathState.ToBucket;
                    }
                    break;
                case ToBucket:
                    //telemetry.addData("ready?", outtake.isLiftReady());
                    if (!follower.isBusy() && outtake.isLiftReady() && elapsedTime==null){
                        outtake.openClaw();
                        elapsedTime = new ElapsedTime();
                    } else if (!follower.isBusy()  && elapsedTime!=null && elapsedTime.milliseconds()> 400){

                        follower.followPath(pickupSample1, true);
                        pathState = PathState.Sample1;
                        elapsedTime =null;
                    }

                    break;
                case Sample1:

                    if (!follower.isBusy() && outtake.isTransferDone() && !outtake.isScoringDone()){

                        pathState= PathState.Score1Lift;
                        elapsedTime= null;
                        count=0;

                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime==null){
                        intake.pickupSampleYellow();

                        elapsedTime= new ElapsedTime();
                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime!=null && elapsedTime.milliseconds()>500 && count==0 ) {
                        intake.extendAutoPickup();
                        count++;
                    } else if (count==1){
//                        intake.incrementExtends();
                    }
                    break;
                case Score1Lift:
                    if (!follower.isBusy()&& count==0) {
                        outtake.scoreSample();
                        count++;
                    } else if (!follower.isBusy() && outtake.isLiftReady() && count==1){
                        pathState= PathState.Score1;
                        follower.followPath(scoreSample1_2);
                        elapsedTimeFollow = new ElapsedTime();
                        elapsedTime = null;
                    }

                    break;
                case Score1:
                    if (elapsedTimeFollow!=null && elapsedTimeFollow.milliseconds()>6000 && follower.isBusy()){
                        //follower.breakFollowing();
                        elapsedTimeFollow=null;
                    }
                    if (!follower.isBusy() && outtake.isLiftReady() && elapsedTime==null){
                        outtake.openClaw();
                        elapsedTime = new ElapsedTime();
                    } else if (!follower.isBusy() && elapsedTime!=null && elapsedTime.milliseconds()> 400){

                        follower.followPath(pickupSample2, true);
                        pathState = PathState.Sample2;
                        count=0;
                        elapsedTime =null;
                    }
                    break;
                case Sample2:
                    if (!follower.isBusy() && outtake.isTransferDone() && !outtake.isScoringDone()){

                     //   follower.followPath(scoreSample2);
                        count=0;
                        pathState= PathState.Score2Lift;
                        elapsedTime= null;

                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime==null){

                        intake.pickupSampleYellow();

                        elapsedTime= new ElapsedTime();
                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime!=null && elapsedTime.milliseconds()>800 && count==0) {
                        intake.extendAutoPickup();
                        count++;
                    }



                    break;
                case Score2Lift:
                    if (!follower.isBusy()&& count==0) {
                        outtake.scoreSample();
                        count++;
                    } else if (!follower.isBusy() && outtake.isLiftReady() && count==1){
                        pathState= PathState.Score2;
                        follower.followPath(scoreSample2);
                        elapsedTimeFollow = new ElapsedTime();
                    }

                    break;
                case Score2:
                    if (elapsedTimeFollow!=null && elapsedTimeFollow.milliseconds()>3000 && follower.isBusy()){
                        //follower.breakFollowing();
                        elapsedTimeFollow=null;
                    }
                    if (!follower.isBusy() && outtake.isLiftReady() && elapsedTime==null){
                        outtake.openClaw();
                        elapsedTime = new ElapsedTime();
                    } else if (!follower.isBusy() && elapsedTime!=null && elapsedTime.milliseconds()> 400){

                        follower.followPath(pickupSample3, true);
                        pathState = PathState.Sample3;
                        elapsedTime =null;
                        count =0;
                    }
                    break;
                case Sample3:
                    if (!follower.isBusy() && outtake.isTransferDone() && !outtake.isScoringDone()){

                       // follower.followPath(scoreSample3);

                        pathState= PathState.Score3LIft;
                        elapsedTime= null;
                        count=0;

                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime==null){

                        intake.pickupSampleYellow();

                        elapsedTime= new ElapsedTime();
                    } else if (!follower.isBusy() && !outtake.isTransferDone() && elapsedTime!=null && elapsedTime.milliseconds()>800 && count==0 ) {
                        intake.extendAutoPickup();
                        count++;
                    }
                    break;
                case Score3LIft:
                    if (!follower.isBusy()&& count==0) {
                        outtake.scoreSample();
                        count++;
                    } else if (!follower.isBusy() && outtake.isLiftReady() && count==1){
                        pathState= PathState.Score3;
                        follower.followPath(scoreSample3);
                        elapsedTimeFollow = new ElapsedTime();
                        elapsedTime = null;
                    }

                    break;
                case Score3:
                    if (elapsedTimeFollow!=null && elapsedTimeFollow.milliseconds()>3000 && follower.isBusy()){
                        //follower.breakFollowing();
                        elapsedTimeFollow=null;
                    }
                    if (!follower.isBusy() && outtake.isLiftReady() && elapsedTime==null){
                        outtake.openClaw();
                        elapsedTime = new ElapsedTime();
                    } else if (!follower.isBusy() && elapsedTime!=null && elapsedTime.milliseconds()> 400){
                       pathState= PathState.Park;
                       elapsedTime = null;
                    }
                    break;

                case Park:
                    follower.followPath(parker);
                    hang.servoReverse();
                    if(elapsedTime == null){
                        elapsedTime = new ElapsedTime();
                    } else if (elapsedTime.milliseconds() > 1000){
                        hang.servoDisable();
                    }
                    break;
            }


            outtake.update();
            intake.update();
            follower.update();
            telemetry.addData("State", pathState);
            telemetry.addData("Outtake", outtake.getStatus());
            telemetry.update();
        }

    }

    protected void buildPaths(){

        scorePreload = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(bucketPose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), bucketPose.getHeading())
                .build();

        pickupSample1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(bucketPose), new Point(sample1)))
                .setLinearHeadingInterpolation(bucketPose.getHeading(), sample1.getHeading())
                .build();


        scoreSample1_1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample1), new Point(midPoint)))
                .setLinearHeadingInterpolation(sample1.getHeading(), midPoint.getHeading())

                .addPath(new BezierCurve( new Point(bucketPose.getX()+1, bucketPose.getY()),new Point(bucketPose)))
                .setLinearHeadingInterpolation(sample1.getHeading(), bucketPose.getHeading())
                .build();

        scoreSample1_2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(sample1),  new Point(bucketPose1)))
                .setConstantHeadingInterpolation(Math.toRadians(-45))
                .build();


        pickupSample2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(bucketPose1), new Point(sample2)))
                .setLinearHeadingInterpolation(bucketPose1.getHeading(), sample2.getHeading())
                .build();


        scoreSample2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample2), new Point(midPoint)))
                .setLinearHeadingInterpolation(sample2.getHeading(), midPoint.getHeading())
                .addPath(new BezierLine(new Point(midPoint),  new Point(bucketPose1)))
                .setConstantHeadingInterpolation(Math.toRadians(-45))
                .build();

        pickupSample3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(bucketPose1), new Point(sample3)))
                .setLinearHeadingInterpolation(bucketPose1.getHeading(), sample3.getHeading())
                .build();


        scoreSample3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(sample3), new Point(bucketPose)))
                .setLinearHeadingInterpolation(sample3.getHeading(), bucketPose.getHeading())
                .build();

        parker = follower.pathBuilder()
                .addPath(new BezierLine(new Point(bucketPose), new Point(park)))
                .setConstantHeadingInterpolation(Math.toRadians(-50))
                .build();
    }
}
