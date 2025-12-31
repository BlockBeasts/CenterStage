//    public static double Blank = 0;//package org.firstinspires.ftc.masters.TeleEx;
//
//import android.os.Environment;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.qualcomm.hardware.lynx.LynxModule;
//import com.qualcomm.hardware.lynx.LynxModuleIntf;
//import com.qualcomm.hardware.lynx.LynxNackException;
//import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
//import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.VoltageSensor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.masters.components.Component;
//import org.firstinspires.ftc.masters.components.Init;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
//import org.joda.time.DateTime;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//@Config // Enables FTC Dashboard
//public class Monitor implements Component {
//
//    private final FtcDashboard dashboard = FtcDashboard.getInstance();
//
//    Init init;
//    Telemetry telemetry;
//
//    LynxGetADCCommand.Channel servoChannel;
//    LynxGetADCCommand servoCommand;
//    LynxGetADCResponse servoResponse;
//
//    VoltageSensor controlHubVoltageSensor, expansionHubVoltageSensor;
//    DcMotorEx leftFrontMotor, rightFrontMotor, leftRearMotor, rightRearMotor, intakeExtendo, outtakeSlideLeft, outtakeSlideRight;
//    LynxModule controlHublynx, expansionHublynx;
//
//    GamepadEx gp1;
//
//    public FileWriter logWriter;
//    ElapsedTime runtime;
//
//    public Monitor(Init init, Telemetry telemetry) {
//
//        this.init = init;
//        this.telemetry = telemetry;
//
//        controlHubVoltageSensor = init.getControlHubVoltageSensor();
//        expansionHubVoltageSensor = init.getExpansionHubVoltageSensor();
//
//        leftFrontMotor = init.getLeftFrontMotor();
//        rightFrontMotor = init.getRightFrontMotor();
//        leftRearMotor = init.getLeftRearMotor();
//        rightRearMotor = init.getRightRearMotor();
//        intakeExtendo = init.getIntakeExtendo();
//        outtakeSlideLeft = init.getOuttakeSlideFront();
//        outtakeSlideRight = init.getOuttakeSlideBack();
//
//        controlHublynx = init.getControlHublynx();
//        expansionHublynx = init.getExpansionHublynx();
//
///    public static double Blank = 0;/        gp1 = init.getGp1();
//
//        initializeHardware(0,0,0);
//
//    }
//
//    @Override
//    public void initializeHardware(double p, double i, double d)  {
//
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//        try {
//            newCSVFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void telemetryPrint(){
//
//            telemetry.addData("Control Hub Voltage", controlHubVoltageSensor.getVoltage());
//            telemetry.addData("Expansion Hub Voltage", expansionHubVoltageSensor.getVoltage());
//
//            telemetry.addData("Control Hub Current", controlHubVoltageSensor.getVoltage());
//            telemetry.addData("Expansion Hub Current", expansionHubVoltageSensor.getVoltage());
//
//            telemetry.addData("LeftFront Current", leftFrontMotor.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("RightFront Current", rightFrontMotor.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("LeftRear Current", leftRearMotor.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("RightRear Current", rightRearMotor.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("intakeExtendo Current", intakeExtendo.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("outtakeLeft Current", outtakeSlideLeft.getCurrent(CurrentUnit.AMPS));
//            telemetry.addData("outtakeRight Current", outtakeSlideRight.getCurrent(CurrentUnit.AMPS));
//
//            telemetry.addData("Control Hub Servo Current", getServoBusCurrent(controlHublynx));
//            telemetry.addData("Expansion Hub Servo Current", getServoBusCurrent(expansionHublynx));
//
//            telemetry.update();
//
//        }
//
//    public double getServoBusCurrent(LynxModuleIntf device){
//
//            servoChannel = LynxGetADCCommand.Channel.SERVO_CURRENT;
//            servoCommand = new LynxGetADCCommand(device, servoChannel, LynxGetADCCommand.Mode.ENGINEERING);
//            try {
//                servoResponse = servoCommand.sendReceive();
//                return servoResponse.getValue() / 1000.0;    // return value in Amps
//            } catch (InterruptedException | RuntimeException | LynxNackException e) {
//            }
//            return 999;
//
//    }
//
//    public void newCSVFile() throws IOException {
//
//        runtime = new ElapsedTime();
//
//        DateTime currentDateAndTime = DateTime.now();
//        File log = new File(Environment.getExternalStorageDirectory().getPath() + "/PowerLogging/" + currentDateAndTime.getMillis() + ".csv");
//
//        logWriter = new FileWriter(log);
//        logWriter.write("Device,Current,Time\n");
//
//    }
//
//    public void writeCSVFile() throws IOException{
//
//        double time = Math.round(runtime.milliseconds());
//
//        logWriter.write("LeftFront," + leftFrontMotor.getCurrent(CurrentUnit.AMPS) + "," + time +  "\n" +
//                            "RightFront," + rightFrontMotor.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "LeftRear," + leftRearMotor.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "RightRear," + rightRearMotor.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "IntakeExtendo," + intakeExtendo.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "OuttakeLeft," + outtakeSlideLeft.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "OuttakeRight," + outtakeSlideRight.getCurrent(CurrentUnit.AMPS) + "," + time + "\n" +
//                            "ControlHubServo," + getServoBusCurrent(controlHublynx) + "," + time + "\n" +
//                            "ExpansionHubServo," + getServoBusCurrent(expansionHublynx) + "," + time + "\n" +
//                            "ControlHubVoltage," + controlHubVoltageSensor.getVoltage() + "," + time + "\n" +
//                            "ExpansionHubVoltage," + expansionHubVoltageSensor.getVoltage() + "," + time + "\n"
//                            );
//
//    }
//
//    public void closeCSVFile() throws IOException{
//
//        logWriter.close();
//
//    }
//
//}