package org.firstinspires.ftc.masters.components;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Hang {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Init init;
    Telemetry telemetry;
    public Outtake outtake;

    CRServo hangLeft, hangRight;

    private ElapsedTime elapsedTime = null;

    private Status status;

    public enum Status {
        Phase1(0),
        Phase2(1000),
        Phase3(1000);

        private final long time;

        Status (long time){
            this.time= time;
        }

        public long getTime() {
            return time;
        }
    }

    public Hang(Init init, Telemetry telemetry){

        this.telemetry = telemetry;
        this.init = init;

        hangLeft = init.getHangLeft();
        hangRight = init.getHangRight();

        initializeHardware();

    }

    public void initializeHardware() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void update(){

        switch (status){
            case Phase1:
                if (elapsedTime == null) {
                    setTarget(ITDCons.BucketTarget);
                    servoEnable();
                    elapsedTime = new ElapsedTime();
                    status = Status.Phase2;
                }
                break;

            case Phase2:
                if (elapsedTime.milliseconds() > status.getTime()) {
                    servoReverse();
                    status = Status.Phase3;
                    elapsedTime = new ElapsedTime();
                }
                break;

            case Phase3:
                if (elapsedTime.milliseconds() > status.getTime()) {
                    setTarget(0);
                }

        }

        outtake.moveSlide();

    }

    public void setTarget(int target){

        outtake.setTarget(target);

    }

    public void servoEnable(){

        hangLeft.setPower(1);
        hangRight.setPower(1);

    }

    public void servoReverse(){

        hangLeft.setPower(-1);
        hangRight.setPower(-1);

    }

    public void servoDisable(){

        hangLeft.setPower(0);
        hangRight.setPower(0);

    }

    public void startHang() {
        status = Status.Phase1;
    }

}
