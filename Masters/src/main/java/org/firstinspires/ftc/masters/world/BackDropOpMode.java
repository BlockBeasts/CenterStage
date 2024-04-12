package org.firstinspires.ftc.masters.world;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.masters.CSCons;
import org.firstinspires.ftc.masters.PropFindRightProcessor;
import org.firstinspires.ftc.masters.drive.SampleMecanumDrive;
import org.firstinspires.ftc.masters.trajectorySequence.TrajectorySequence;
import org.firstinspires.ftc.masters.world.paths.BlueBackDropPath;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.Date;
import java.util.List;

public abstract class BackDropOpMode extends LinearOpMode {

    protected OpenCvCamera webcam;

    protected static final int CAMERA_WIDTH  = 640; // width  of wanted camera resolution
    protected static final int CAMERA_HEIGHT = 360; // height of wanted camera resolution

    protected TelemetryPacket packet = new TelemetryPacket();

    int cycleCount=0;

    public enum State {
        PURPLE_DEPOSIT_PATH,
        PURPLE_DEPOSIT,

        TO_STACK,
        TO_STACK_TAG,
        TO_BACKBOARD,

        BACKDROP_DEPOSIT_PATH,

        TO_PARK,

        PARK,
        LOWER,
        END
    }

    protected ElapsedTime purpleDepositTime = null;
    protected ElapsedTime depositTime = null;
    protected ElapsedTime dropTime = null;
    protected ElapsedTime pickupElapsedTime= null;

    protected CSCons.OuttakeWrist outtakeWristPosition = CSCons.OuttakeWrist.vertical;

    protected SampleMecanumDrive drive;

    protected PropFindRightProcessor.pos propPos = null;

    protected TrajectorySequence rightPurple, leftPurple, middlePurple;
    protected TrajectorySequence rightYellow, leftYellow, midYellow;
    protected TrajectorySequence toStackFromRight, toStackFromLeft, toStackFromMid;
    protected TrajectorySequence toBackBoard;
    protected TrajectorySequence park;

    protected State currentState;
    protected int outtakeTarget = 0;

    protected boolean switched= false;

    protected void initAuto(){

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        drive = new SampleMecanumDrive(hardwareMap, telemetry);
        drive.initializeAprilTagProcessing();
        initializeProp();
        drive.initializeVisionPortal();

        long startTime = new Date().getTime();
        long time = 0;

        VisionPortal visionPortal = drive.getMyVisionPortal();
        while(time<2000 && opModeIsActive()){
//        while(visionPortal.getCameraState()!= VisionPortal.CameraState.CAMERA_DEVICE_READY
//                 && opModeIsActive()){
            time = new Date().getTime() - startTime;
            telemetry.addData("camera state", visionPortal.getCameraState());
            telemetry.update();
        }


//        drive.activateFrontCamera();
        drive.enablePropProcessor();

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        drive.raiseIntake();
        drive.closeFingers();


    }

    protected void initializeProp(){

    }

    protected void retrievePropPos(){
        long startTime = new Date().getTime();
        long time = 0;

        while (time < 50 && opModeIsActive()) {
            time = new Date().getTime() - startTime;
            propPos = drive.getPropFindProcessor().position;
            telemetry.addData("Position", propPos);
        }

        drive.enableAprilTag();

    }

    protected void purpleDepositPathState(){
        if (propPos == PropFindRightProcessor.pos.LEFT) {
            drive.followTrajectorySequenceAsync(leftPurple);

        } else if (propPos == PropFindRightProcessor.pos.RIGHT) {
            drive.followTrajectorySequenceAsync(rightPurple);

        } else {
            drive.followTrajectorySequenceAsync(middlePurple);
        }

        currentState = State.PURPLE_DEPOSIT;
    }

    protected void purpleDepositState(){
//        if (!switched) {
//            drive.activateBackCamera();
//            switched=true;
//        }

        if (!drive.isBusy()){
            if (purpleDepositTime ==null){
                drive.raiseIntake();
                outtakeWristPosition = CSCons.OuttakeWrist.flatRight;
                drive.outtakeToBackdrop();
                purpleDepositTime = new ElapsedTime();
            } else if (purpleDepositTime.milliseconds()>100) {

                switch(propPos){
                    case RIGHT:
                        drive.followTrajectorySequenceAsync(rightYellow);
                        break;
                    case LEFT:
                        drive.followTrajectorySequenceAsync(leftYellow);
                        break;
                    case MID:
                        drive.followTrajectorySequenceAsync(midYellow);
                        break;
                }
                outtakeTarget = CSCons.OuttakePosition.AUTO.getTarget();
                currentState=State.BACKDROP_DEPOSIT_PATH;
            }
        }
    }

    protected void backdropDepositPath(State nextState, TrajectorySequence nextPath){

        if (cycleCount>=1) {

            if (pickupElapsedTime != null && pickupElapsedTime.milliseconds() > 250) {
                drive.closeFingers();
                drive.revertIntake();
                pickupElapsedTime = null;
            }
            if (drive.getPoseEstimate().getX()>25){
                outtakeTarget = CSCons.OuttakePosition.AUTO.getTarget();
                drive.closeFingers();
                if (drive.getBackSlides().getCurrentPosition()>outtakeTarget- 200){
                    drive.outtakeToBackdrop();
                    drive.setWristServoPosition(CSCons.OuttakeWrist.flatRight);
                } else if (drive.getBackSlides().getCurrentPosition()>10){
                    drive.outtakeToBackdrop();
                }
                drive.stopIntake();
            }
        } else {

            if (drive.getBackSlides().getCurrentPosition() > outtakeTarget - 150) {
                drive.setWristServoPosition(CSCons.OuttakeWrist.flatRight);
            }
        }

        if (!drive.isBusy()){
            drive.activateFrontCamera();
            drive.enableAprilTag();

            drive.openFingers();
            if (depositTime==null){
                depositTime= new ElapsedTime();
            } else if (depositTime.milliseconds()>100){
                dropTime = new ElapsedTime();
                if (nextState == State.PARK){
                    drive.followTrajectorySequenceAsync(park);
                } else if (nextState == State.TO_STACK){
                    drive.intakeOverStack();

                    drive.followTrajectorySequenceAsync(nextPath);
                }
                cycleCount++;
                currentState= nextState;
            }
        }
    }

    protected void toStack(){

        if (dropTime!=null && dropTime.milliseconds()>300){
            outtakeTarget=0;
            drive.outtakeToTransfer();
        }

        if (!drive.isBusy()){
            List<AprilTagDetection> currentDetections = drive.getAprilTag().getDetections();
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                } else {
                    telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
                }
            }   // end for() loop
            telemetry.addData("April Tag Pos Es", drive.aprilTagCoarsePosEstimate(currentDetections));
             Pose2d robotPosition =drive.aprilTagCoarsePosEstimate(currentDetections);
             while (robotPosition.epsilonEquals(drive.getPoseEstimate())){
                 currentDetections = drive.getAprilTag().getDetections();
                 for (AprilTagDetection detection : currentDetections) {
                     if (detection.metadata != null) {
                         telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                         telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                         telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                         telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                     } else {
                         telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                         telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
                     }
                 }   // end for() loop
                 telemetry.addData("April Tag Pos Es", drive.aprilTagCoarsePosEstimate(currentDetections));
                 robotPosition =drive.aprilTagCoarsePosEstimate(currentDetections);
             }
            currentState= State.TO_STACK_TAG;
            drive.setPoseEstimate(robotPosition);
            drive.followTrajectorySequenceAsync(BlueBackDropPath.toStackWing(drive, robotPosition));



        }
    }

    protected void toStackTag(){

        if (!drive.isBusy()){
            if(pickupElapsedTime==null) {
                drive.intakeToTopStack();
                pickupElapsedTime = new ElapsedTime();
                drive.getMyVisionPortal().getActiveCamera().close();
            }
            //            if (has2Pixels() ){
//                pickupElapsedTime = new ElapsedTime();
//            }
//
//            if (pickupElapsedTime!=null && (pickupElapsedTime.milliseconds()>1000 || (has2Pixels() && pickupElapsedTime.milliseconds()>100))  ){
//                drive.stopIntake();
//                drive.raiseIntake();
//                drive.outtakeToPickup();
//                pickupElapsedTime = new ElapsedTime();
//                currentState = BackDropOpMode.State.BACKDROP_DEPOSIT_PATH;
//                drive.followTrajectorySequenceAsync(toBackBoard);
//
//            }
        }
    }

    protected  void park(){
        if (!drive.isBusy()){
            outtakeTarget = 0;
            drive.outtakeToTransfer();
        }

    }

    protected boolean has2Pixels(){
        return !drive.frontBreakBeam.getState() && !drive.backBreakBeam.getState();
    }


}