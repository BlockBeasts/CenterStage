package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Intake {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    PIDController pidController;

    public static double p = 0.0007, i = 0, d = 0;
    public static double f = 0;

    public static double ticks_in_degrees = (double) 8192 / 360;

    Init init;

    Telemetry telemetry;
    Servo intakeArm;
    Servo intakeChain;
    CRServo intake;
    DcMotor extendo;
    RevColorSensorV3 colorSensor;
    Servo led;
    Servo pusher;
    Outtake outtake;
    ITDCons.Color allianceColor;
    ITDCons.Color color;

    public double multiplier = 1;

    public static double INTAKE_POWER = -1;
    public static double EJECT_POWER = 1;

    public enum Status{
        TRANSFER(0),
        PICKUP_YELLOW(0),
        PICKUP_ALLIANCE(0),
        INIT(0),
        EJECT(500),
        TO_TRANSFER(0),
        TO_NEUTRAL(0),
        EXTEND_TO_HUMAN(0),
        EJECT_TO_HUMAN(800),
        NEUTRAL(0);

        private final long time;
        Status(long time){
            this.time= time;
        }
        public long getTime() {
            return time;
        }
    }

    ElapsedTime elapsedTime = null;
    ElapsedTime closingTime = null;

    public  Status status;
    Gamepad gamepad1;

    private int target;

    public int checkColorCount=0;
    public int MAX_COUNT=5;
    public int redTotal =0;
    public int blueTotal =0;
    public int greenTotal=0;

    public Intake(Init init, Telemetry telemetry){
        this.telemetry = telemetry;
        this.init = init;

        intakeArm = init.getIntakeArm();
        intakeChain = init.getIntakeChain();
        intake = init.getIntake();
        extendo = init.getIntakeExtendo();
        colorSensor = init.getColor();
        led = init.getLed();
        pusher = init.getPusherServo();

        status = Status.INIT;
        color= ITDCons.Color.unknown;
        pidController = new PIDController(p,i,d);
        target =0;
    }

    public void setAllianceColor(ITDCons.Color color){
        allianceColor = color;
    }

    public void setGamepad1(Gamepad gamepad){
        this.gamepad1= gamepad;
    }

    public void initStatusTeleop(){
        status = Status.NEUTRAL;
        color= ITDCons.Color.unknown;
        servoToNeutral();
    }

    protected void pickupSample(){
        status= Status.PICKUP_YELLOW;

        servoToDrop();
        startIntake();

    }

    public void pickupSampleYellow(){
        elapsedTime = null;
        status = Status.PICKUP_YELLOW;
        color= ITDCons.Color.unknown;
    }

    public void pickupSampleAlliance(){
        pickupSample();
        status = Status.PICKUP_ALLIANCE;
    }

    public void stopPickup(){
        if (status == Status.PICKUP_YELLOW){
            status= Status.TO_TRANSFER;
            elapsedTime =null;
        }
        if (status== Status.PICKUP_ALLIANCE){
            elapsedTime =null;
            status = Status.TO_NEUTRAL;
        }
    }

    public void toTransfer(){
        status = Status.TO_TRANSFER;
        elapsedTime = null;
    }

    protected void startIntake (){
        intake.setPower(INTAKE_POWER);
    }

    public void stopIntake(){
        intake.setPower(0);
    }

    public void ejectIntake(){
        status=Status.EJECT;
        elapsedTime = null;
    }

    public void retractSlide() {
        target =0;
    }

    public void extendSlideHumanPlayer(){
        status= Status.EXTEND_TO_HUMAN;
    }

    public void extendSlideHalf() {
        target = ITDCons.halfExtension;
        multiplier=1;
        servoToNeutral();
    }

    public void extendSlideHalfAuto(){
        target= ITDCons.MaxExtension;
        multiplier=1;
    }

    public void extendAutoPickup(){
        target = ITDCons.halfExtension+2000;
        multiplier=0.2;
    }

    public void incrementExtends(){
        multiplier=1;
        if (target<ITDCons.MaxExtension-100) {
            target += 100;
        }
    }

    public void extendSlideMax(){
        target= ITDCons.MaxExtension;
        multiplier =1;
        servoToNeutral();
    }

    public void extendSlideMAxAuto(){
        target= ITDCons.MaxExtension-3000;
        multiplier =0.3;
    }

    public void transferDone(){
        status = Status.NEUTRAL;
        stopIntake();
        servoToTransfer();
    }

    public void extendForTransfer(){
        target = ITDCons.TransferExtensionOut;
    }

    public void pushOut(){
        pusher.setPosition(ITDCons.pushOut);
    }

    public void pushIn(){
        pusher.setPosition(ITDCons.pushIn);
    }

    public void update(){

            int pos = extendo.getCurrentPosition();
            double pid = pidController.calculate(pos, target);

            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

            double lift = pid + ff;

            extendo.setPower(lift*multiplier);

            switch (status){
                case EXTEND_TO_HUMAN:
                    if (pos <ITDCons.MaxExtension-100) {
                        target = ITDCons.MaxExtension;
                        servoToDrop();
                    } else  {
                        intake.setPower(ITDCons.intakeEjectSpeed);
                        status= Status.EJECT_TO_HUMAN;
                        elapsedTime = new ElapsedTime();
                        color= ITDCons.Color.unknown;
                    }
                    break;
                case EJECT_TO_HUMAN:
                    if (elapsedTime!=null && elapsedTime.milliseconds()> status.getTime()){
                        intake.setPower(0);
                        servoToNeutral();
                        target = ITDCons.halfExtension;;
                        elapsedTime=null;
                        color= ITDCons.Color.unknown;
                        status = Status.NEUTRAL;
                    }
                    break;

                case EJECT:
                    if (elapsedTime==null ){
                        elapsedTime= new ElapsedTime();
                        intake.setPower(EJECT_POWER);
                        color = ITDCons.Color.unknown;
                    }

                    if ( elapsedTime.milliseconds()> status.getTime()){
                        intake.setPower(0);
                        servoToNeutral();
                        elapsedTime=null;
                        color= ITDCons.Color.unknown;
                        status= Status.NEUTRAL;

                    }
                    break;

                case PICKUP_YELLOW: //get samples from submersible
                    if (elapsedTime == null) {
                        servoToDrop();
                        startIntake();
                        color = ITDCons.Color.unknown;
                        elapsedTime = new ElapsedTime();
                    }

                    if (colorSensor.rawOptical()>175 && checkColorCount<MAX_COUNT){
                        checkColor();
                    }
                    if (checkColorCount==MAX_COUNT) {
                        checkColor();

                        if (color != ITDCons.Color.unknown && color != ITDCons.Color.yellow && color != allianceColor) {
                            ejectIntake();
                        } else if (color == ITDCons.Color.yellow) {
                            status = Status.TO_TRANSFER;
                            elapsedTime = null;
//                            if (gamepad1!=null) {
//                                gamepad1.rumble(2000);
//                            }

                        } else if (color == allianceColor) {
                            status = Status.TO_TRANSFER;
                            elapsedTime = null;
//
//                            if (gamepad1!=null) {
//                                gamepad1.rumble(2000);
//                            }
                        }
                        resetColorDetection();

                    } else{
                        led.setPosition(ITDCons.off);
                        color = ITDCons.Color.unknown;
                    }

                    break;
                case PICKUP_ALLIANCE:
                    if (elapsedTime == null) {
                        servoToDrop();
                        startIntake();
                        color = ITDCons.Color.unknown;
                        elapsedTime = new ElapsedTime();
                    }
                    if (colorSensor.rawOptical()>175 && checkColorCount<MAX_COUNT){
                        checkColor();
                    }
                    if (checkColorCount==MAX_COUNT) {
                        checkColor();

                        //read color
                        if (color != ITDCons.Color.unknown && (color == ITDCons.Color.yellow || color != allianceColor)) {
                            ejectIntake();
                        } else if (color == allianceColor) {
                            elapsedTime =null;
                            status = Status.TO_NEUTRAL;
                        }
                        resetColorDetection();

                    } else{
                        led.setPosition(ITDCons.off);
                        color = ITDCons.Color.unknown;
                    }
                    break;

                case TO_TRANSFER:
                    if (elapsedTime ==null){

                        if (target==0){
                            target = ITDCons.TransferExtensionOut;
                        } else {
                            servoToTransfer();
                            if (target==ITDCons.MaxExtension){
                                target= ITDCons.TransferExtensionIn;
                            }
                            if (target==ITDCons.halfExtension){
                                target= ITDCons.TransferExtensionIn;
                            }
                        }
                        elapsedTime = new ElapsedTime();
                    }
                    if (elapsedTime.milliseconds()>status.getTime() && elapsedTime.milliseconds()< status.getTime()*2){
                        if (target == ITDCons.TransferExtensionOut){
                            servoToTransfer();
                        }
                        if (target>ITDCons.TransferExtensionIn){
                            target = ITDCons.TransferExtensionIn;
                        }
                    }
                    // https://www.youtube.com/watch?v=xGytDsqkQY8
                    if (extendo.getCurrentPosition()<1000){
                        telemetry.addData("Currect Pos: ", true);
                        if (closingTime==null){
                            closingTime = new ElapsedTime();
                        } else {
                            if (closingTime.milliseconds() > 300){
                                elapsedTime = null;
                                closingTime = null;
                                status = Status.TRANSFER;
                            }
                        }
                    }

                    // if (...<200){ if (closing==null){closingTime= new elapsedTime) else {if closingTime>...){status =....

                    break;

                case TO_NEUTRAL:
                    if (elapsedTime==null){
//                        stopIntake();
                        servoToNeutral();
                        if (target == ITDCons.MaxExtension){
                            target = ITDCons.halfExtension;
                        } else if (target == ITDCons.halfExtension){
                            target =0;
                        }
                        status = Status.NEUTRAL;
                    }
                    break;

            }
            telemetry.addData("intake status", status);

    }

    public void setTarget(int target){
        this.target= target;
    }

    public void led(){
        led.setPosition(1);
    }

    public void checkColor(){
        if (checkColorCount<MAX_COUNT){
            redTotal+=colorSensor.red();
            blueTotal+=colorSensor.blue();
            greenTotal+=colorSensor.green();
            checkColorCount++;

            telemetry.addData("red", redTotal);
            telemetry.addData("green", greenTotal);
            telemetry.addData("blue", blueTotal);
            telemetry.addData("count", checkColorCount);
        } else {

            if (redTotal>blueTotal && redTotal> greenTotal){
                led.setPosition(ITDCons.red);
                color = ITDCons.Color.red;
            }
            else if (greenTotal>blueTotal && greenTotal>redTotal){
                led.setPosition(ITDCons.yellow);
                color = ITDCons.Color.yellow;
            } else if (blueTotal>greenTotal && blueTotal>redTotal){
                led.setPosition(ITDCons.blue);
                color = ITDCons.Color.blue;
            } else {
                led.setPosition(ITDCons.off);
                color = ITDCons.Color.unknown;
            }
        }
    }

    public ITDCons.Color getColor(){
        return color;
    }

    public Status getStatus(){
        return status;
    }

    public void setOuttake(Outtake outtake){
        this.outtake= outtake;
    }

    public boolean readyToTransfer(){
        return status == Status.TRANSFER;
    }

    public void intakeintake(){
        intakeArm.setPosition(ITDCons.intakeintakearm);
        intakeChain.setPosition(ITDCons.intakeintakechain);
    }

    public void servoToDrop(){
        intakeArm.setPosition(ITDCons.intakeArmDrop);
        intakeChain.setPosition(ITDCons.intakeChainDrop);
    }

    public void servoToTransfer(){
        intakeArm.setPosition(ITDCons.intakeArmTransfer);
        intakeChain.setPosition(ITDCons.intakeChainTransfer);
    }

    public void servoToNeutral(){
        intakeArm.setPosition(ITDCons.intakeArmNeutral);
        intakeChain.setPosition(ITDCons.intakeChainNeutral);
    }

    protected void resetColorDetection(){
        checkColorCount=0;
        redTotal=0;
        blueTotal=0;
        greenTotal=0;
    }

}
