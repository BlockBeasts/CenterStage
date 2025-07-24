package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Outtake implements Component{

    private PIDController controller;
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double p = 0.0007, i = 0, d = 0.00001;

    public static int target = 0;

    public int offset =0;

    private final Servo claw, led;
    public final Servo armPosition, clawPosition;
    private final DcMotor outtakeSlideLeft;
    private final DcMotor outtakeSlideRight;
    private final DcMotor outtakeSlideCenter;
    public final DcMotor outtakeSlideEncoder;

    public int slideIncrement = 0;
    public boolean slideFixed = false;

    VoltageSensor voltageSensor;

    private Status status;

    private ElapsedTime elapsedTime = null;

    protected boolean isLiftReady = false;
    protected boolean isScoringDone = false;

    public boolean drivetrainOverride = false;

    public Intake intake;
    public DriveTrain driveTrain;
    public boolean transferDone= false;


    Telemetry telemetry;
    Init init;
    Gamepad gamepad1;

    enum Mode {
        Sample,
        Specimen
    }

    enum WaitTime{
        Open_Claw (350), CLose_Claw(400)
        , Turn_Wrist(600), BackUp_Robot(400),
        Servo_To_Transfer(300),
        Move_Position(450)
        ;

        private final long time;

        private WaitTime(long time){
            this.time= time;
        }

        public long getTime() {
            return time;
        }
    }

    public enum Status {
        TransferReady(0),
        TransferDone(0),

        ScoreSpecimen(0),
        Wall (0),
        Bucket(0),
        InitWall(0),
        InitAutoSpec(0),
        InitAutoSample(0),

        TransferToBucket_Back(0),
        TransferToBucket_Lift(0),
        TransferToBucket_Move(0),
        AutoLiftToBucket(0),
//        ScoreSample(100),
//        ScoreSampleOpenClaw(200),
        ScoringSampleDone(0),

        TransferToWall_Up(0),
        TransferToWall_Back(0),
        TransferToWall_Final(0),

//        BucketToTransfer_Down(500),
//        BucketToTransfer_Open(200),
        BucketToTransfer_Final(0),

        WallToFront_lift(0),
        WallToFront_move(0),
        WallToFront3 (0),

        WallToTransfer1(0), //close claw and move angle servo
         WallToTransfer2(0),//go to transfer position and open
        CloseClawTransfer(0),

        Specimen_To_Wall(0),
        SpecimenToWall_MoveBack(0),

        Transfer_To_Wall(0),
        ToBucketClose(0),

        CloseClawSpec(0);



        private final long time;

        private Status (long time){
            this.time= time;
        }

        public long getTime() {
            return time;
        }
    }

    public Outtake(Init init, Telemetry telemetry){

        this.init=init;
        this.telemetry=telemetry;

        claw= init.getClaw();
        this.outtakeSlideLeft =init.getOuttakeSlideFront();
        this.outtakeSlideRight =init.getOuttakeSlideBack();
        this.outtakeSlideCenter =init.getOuttakeSlideMiddle();
        this.outtakeSlideEncoder =init.getOuttakeSlideFront();
        voltageSensor = init.getControlHubVoltageSensor();
        armPosition = init.getArmPosition();
        clawPosition = init.getClawPosition();
        led = init.getLed();

        initializeHardware(p, i, d);
        status= Status.InitWall;

    }

    public void setGamepad(Gamepad gamepad){
        this.gamepad1 = gamepad;
    }

    public void setIntake(Intake intake){
        this.intake= intake;
    }

    public void setDriveTrain(DriveTrain driveTrain){
        this.driveTrain = driveTrain;
    }


    public void initializeHardware(double p, double i, double d) {

        controller = new PIDController(p, i, d);
        controller.setPID(p, i, d);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        target = 0;

    }

    public void initTeleopWall(){
        armPosition.setPosition(ITDCons.angleWallSpecArm);
        clawPosition.setPosition(ITDCons.angleWallSpecClaw);
        claw.setPosition(ITDCons.clawOpen);
        status= Status.InitWall;
    }

    public void initAutoSpecimen(){
        //position.setPosition(ITDCons.positionInitSpec);
        setAngleServoScore();
        closeClaw();
        status= Status.InitAutoSpec;
    }

    public void initAutoSample(){
        armPosition.setPosition(ITDCons.angleWallSpecArm - .1);
        clawPosition.setPosition(ITDCons.angleWallSpecClaw);
        closeClaw();
        status= Status.InitAutoSample;
    }

    public void openClaw() {

        transferDone = false;
        if (status==Status.ScoreSpecimen){
            status= Status.Specimen_To_Wall;
            elapsedTime = null;

        } else if (status == Status.Bucket){
            elapsedTime= null;
            claw.setPosition(ITDCons.clawOpen);
            status = Status.ScoringSampleDone;
        } else {
           claw.setPosition(ITDCons.clawOpen);
           if (status == Status.TransferDone){
               status= Status.TransferReady;
           }
        }
    }

    public void openClawAuto() {

        claw.setPosition(ITDCons.clawOpen);

    }

    public void wallGrabAuto() {

        if (status==Status.ScoreSpecimen) {
            claw.setPosition(ITDCons.clawOpen);
            moveToPickUpFromWall();
        }
    }

    public void closeClaw() {
        claw.setPosition(ITDCons.clawClose);
        if(status == Status.TransferReady){
            status = Status.CloseClawTransfer;
            elapsedTime = new ElapsedTime();
        } else if (status==Status.Wall){
            status = Status.CloseClawSpec;
            elapsedTime= new ElapsedTime();
        }
    }

    public void moveToPickUpFromWall(){
        if (status==Status.ScoreSpecimen) {

            status= Status.Specimen_To_Wall;
            elapsedTime = new ElapsedTime();
        }
        if (status == Status.InitWall){
            target = ITDCons.WallTarget;
            claw.setPosition(ITDCons.clawOpen);

            status= Status.Wall;
        }
        if (status==Status.TransferReady){

            elapsedTime = null;
            status= Status.Transfer_To_Wall;

        }
    }

    public void moveToTransfer(){

        if (status==Status.Wall || status == Status.InitWall){

            setAngleServoToMiddle();
            closeClaw();
            target = ITDCons.TransferTarget; //set higher
            elapsedTime = new ElapsedTime();
            status = Status.WallToTransfer1;


        } else if (status==Status.ScoringSampleDone) {
//            closeClaw();
//            target = ITDCons.intermediateTarget;
//            position.setPosition(ITDCons.positionTransfer);
//            setAngleServoScore();
            status = Status.BucketToTransfer_Final;
        }

    }

    public void moveToTransferTest(){

            armPosition.setPosition(ITDCons.angleTransferArm);
            clawPosition.setPosition(ITDCons.angleTransferClaw);
            claw.setPosition(ITDCons.clawOpen);
            target=ITDCons.TransferTarget;

    }


    public void moveToBucket(){
        closeClaw();
        status = Status.TransferToBucket_Back;
    }

    public void score(){

        if (status== Status.Wall){
            scoreSpecimen();
        } else if (status== Status.TransferDone){
            scoreSample();
        }

    }


    public void scoreSpecimen(){
        if (status==Status.InitAutoSpec){
            status= Status.WallToFront_lift;
            elapsedTime = new ElapsedTime();
        } else {
            status = Status.WallToFront_lift;
            elapsedTime = null;
        }

        target = ITDCons.SpecimenTarget;

    }

    public void scoreSample(){
        if (status == Status.InitAutoSample){

            setAngleServoScoreSample();
            status = Status.AutoLiftToBucket;
        } else{
            closeClaw();
            setAngleServoScoreSample();

            status = Status.ToBucketClose;
        }

        elapsedTime = new ElapsedTime();
        target = ITDCons.BucketTarget;
    }

    public void scoreSampleLow(){
        if (status == Status.InitAutoSample){

            setAngleServoScoreSample();
            status = Status.AutoLiftToBucket;
        } else{
            closeClaw();
            setAngleServoScoreSample();

            status = Status.ToBucketClose;
        }
        elapsedTime = new ElapsedTime();
        target = ITDCons.LowBucketTarget;

    }


    public void moveSlide() {

//frontRight
        int rotatePos = -(outtakeSlideEncoder.getCurrentPosition());

//        telemetry.addData("rotatePos",rotatePos);
        double lift = controller.calculate(rotatePos, target-offset);
       // double lift = pid + f;

//        telemetry.addData("lift", lift);

        outtakeSlideLeft.setPower(lift);
        outtakeSlideRight.setPower(lift);
        outtakeSlideCenter.setPower(lift);

    }

    public int getTarget(){
        return target;
    }

    public void setTarget(int target){
        this.target = target;
    }

    public int getLiftPos(){
        return -outtakeSlideEncoder.getCurrentPosition();
    }

//    public void releaseSample(){
//
//        claw.setPosition(ITDCons.clawOpen);
//        isScoringDone=true;
//
//    }


    public void update(){
        moveSlide();

//        telemetry.addData("status", status);
//        telemetry.addData("target", target);

        switch (status){
            case WallToFront_lift:
                if (elapsedTime ==null) {
                    target = ITDCons.SpecimenTarget;
                    setAngleServoToMiddle();
                    elapsedTime = new ElapsedTime();
                }
                if (elapsedTime.milliseconds()> status.getTime() && elapsedTime.milliseconds()< status.getTime()+WaitTime.Move_Position.getTime()){

                }
                if (elapsedTime.milliseconds()> status.getTime()+WaitTime.Move_Position.getTime()){
                    setAngleServoScore();
                    elapsedTime = null;
                    status = Status.ScoreSpecimen;
                }

                break;

            case TransferToBucket_Back:
                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()){
                    if (target!=ITDCons.LowBucketTarget && target!=ITDCons.BucketTarget) {
                        target = ITDCons.BucketTarget;
                    }
                    setAngleServoScoreSample();
                    elapsedTime= new ElapsedTime();
                    status = Status.TransferToBucket_Lift;
                    isScoringDone= false;
                    isLiftReady = true;
                }
                break;

            case TransferToBucket_Lift:

                //status= Status.Bucket;
//                isLiftReady = true;
               //TODO: change from time to vertical slide position
                if (getLiftPos()>target-3000){
                    elapsedTime = new ElapsedTime();
                    status = Status.Bucket;
                    isLiftReady = true;
                }
                break;
            case TransferToBucket_Move:
                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()) {
                    elapsedTime = null;
                    status = Status.Bucket;
                }
                break;

            case AutoLiftToBucket:

                if (getLiftPos()>ITDCons.BucketTarget-3000) {
                    isLiftReady = true;
                    status=Status.Bucket;
                }
                break;


            case ScoringSampleDone:
                if (elapsedTime== null){
                    claw.setPosition(ITDCons.clawOpen);
                    elapsedTime = new ElapsedTime();
                }
                if ( elapsedTime.milliseconds()>WaitTime.Open_Claw.getTime() && elapsedTime.milliseconds()<WaitTime.Open_Claw.getTime()+WaitTime.BackUp_Robot.getTime() ){
                    if (driveTrain!=null) {
                        //driveTrain.drive(0.6);
                        //drivetrainOverride = true;
                    }
                }
                if (elapsedTime.milliseconds()>WaitTime.Open_Claw.getTime()+WaitTime.BackUp_Robot.getTime()){
                    if (driveTrain!=null) {
                        //drivetrainOverride = false;
                        //driveTrain.drive(0);
                    }
                    status= Status.BucketToTransfer_Final;
                    elapsedTime = null;
                }

                break;

            case BucketToTransfer_Final:
                if (elapsedTime==null){
                    closeClaw();
                    target = ITDCons.TransferTarget;
                    setAngleServoToMiddle(); // TODO: hit dat middle
                    intake.setTarget(ITDCons.halfExtension);
                    elapsedTime = new ElapsedTime();
                }
                if (elapsedTime.milliseconds()>status.getTime() && elapsedTime.milliseconds()<WaitTime.Servo_To_Transfer.getTime()+ status.getTime()){
                    setAngleServoToTransfer();
                }
                if (elapsedTime.milliseconds()>WaitTime.Servo_To_Transfer.getTime()+status.getTime()){
                    openClawAuto();
                    status= Status.TransferReady;
                    elapsedTime = null;
                }
                break;

            case WallToTransfer1:
                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()){
                    status = Status.WallToTransfer2;
                    elapsedTime= new ElapsedTime();
                }

            case WallToTransfer2:
                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()) {
                    setAngleServoToTransfer();
                    openClawAuto();
                    status = Status.TransferReady;
                }

                break;

            case Specimen_To_Wall:
                if (elapsedTime ==null){
                    elapsedTime = new ElapsedTime();
                    openClawAuto();
                }
                if (elapsedTime.milliseconds()>WaitTime.Open_Claw.getTime() && elapsedTime.milliseconds()<WaitTime.Turn_Wrist.getTime()){
                    target = ITDCons.intermediateTarget;
                    setAngleServoToMiddle();
                }
                if (elapsedTime.milliseconds()>WaitTime.Turn_Wrist.getTime()){
                    elapsedTime =new ElapsedTime();
                    status = Status.SpecimenToWall_MoveBack;
                }

               break;

            case SpecimenToWall_MoveBack:

                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime() ) {
                    target = ITDCons.wallPickupTarget;

                    setAngleServoToBack();
                    openClaw();
                    status= Status.Wall;
                } else if (elapsedTime!=null && elapsedTime.milliseconds()>200){
                }
                break;

            case Transfer_To_Wall:
                if (elapsedTime == null){
                    setAngleServoToMiddle();
                    closeClaw();
                    elapsedTime = new ElapsedTime();

                }
                if (elapsedTime.milliseconds()>WaitTime.CLose_Claw.getTime()){
                    target = ITDCons.WallTarget;
                    status = Status.SpecimenToWall_MoveBack;
                    elapsedTime = new ElapsedTime();
                }
                break;

            case TransferReady:

                //put back code when position is consistent
                    if (intake.readyToTransfer()){
                        closeClaw();
                        status= Status.CloseClawTransfer;
                        elapsedTime = new ElapsedTime();
                    }

                break;
            case CloseClawTransfer:
                if (elapsedTime!=null && elapsedTime.milliseconds()> status.getTime()){ //&& elapsedTime.milliseconds()<status.getTime()+300
                    intake.extendForTransfer();

                }

                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()+300){
                    status= Status.TransferDone;
                    transferDone= true;
//                    if (gamepad1!=null) {
//                        gamepad1.rumble(1500);
//                    }
                    intake.transferDone();
                    scoreSample();
                }
                break;

            case CloseClawSpec:
                if (elapsedTime!=null && elapsedTime.milliseconds()>WaitTime.CLose_Claw.getTime()){
                    scoreSpecimen();
                }
                break;

            case ToBucketClose:
                if (elapsedTime!=null && elapsedTime.milliseconds()>status.getTime()){

                    status = Status.TransferToBucket_Back;
                    elapsedTime= new ElapsedTime();
                }
                break;

        }
    }

    private void setAngleServoToTransfer(){
        armPosition.setPosition(ITDCons.angleTransferArm);
        clawPosition.setPosition(ITDCons.angleTransferClaw);
    }

    private void setAngleServoToMiddle(){
        armPosition.setPosition(ITDCons.angleMiddleArm);
        clawPosition.setPosition(ITDCons.angleMiddleClaw);
    }

    public void setAngleServoToBack(){
        armPosition.setPosition(ITDCons.angleWallSpecArm);
        clawPosition.setPosition(ITDCons.angleWallSpecClaw);
    }

    private void setAngleServoScore(){
        armPosition.setPosition(ITDCons.angleScoreSpecArm);
        clawPosition.setPosition(ITDCons.angleScoreSpecClaw);
    }

    private void setAngleServoScoreSample(){
        armPosition.setPosition(ITDCons.angleWallSpecArm - .1);
        clawPosition.setPosition(ITDCons.angleWallSpecClaw);
    }

    private void resetSlides(){
        if (!slideFixed) {
            slideIncrement = slideIncrement - 100;
            setTarget(slideIncrement);
            if (voltageSensor.getVoltage() < 9) {
                outtakeSlideLeft.setPower(0);
                outtakeSlideRight.setPower(0);
                outtakeSlideEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                outtakeSlideEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideFixed = true;
            }
        }
        if (slideFixed){
            led.setPosition(1);
        }
    }

//
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }

    public boolean isLiftReady(){
        return isLiftReady;
    }

    public boolean isScoringDone(){
        return  isScoringDone;
    }

    public boolean isReadyToPickUp(){
        return  status== Status.Wall;
    }

    public void setLiftReady(boolean liftReady) {
        isLiftReady = liftReady;
    }

    public boolean isReadyForTransfer(){
        return status==Status.TransferReady;
    }

    public Status getStatus(){
        return status;
    }

    public  boolean isTransferDone() {
        return  transferDone;

    }

}
