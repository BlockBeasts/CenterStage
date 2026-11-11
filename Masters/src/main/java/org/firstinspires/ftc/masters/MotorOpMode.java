package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.mechanisms.ProgrammingBoard1;

@TeleOp
public class MotorOpMode extends OpMode {
    double strafe = 0.25;
    ProgrammingBoard1 board = new ProgrammingBoard1();
    boolean inEndGame;
    double endGameTime;
    @Override
    public void init() {
        board.init(hardwareMap);
        inEndGame = false;
    }
    @Override
    public void start() {
        endGameTime = getRuntime() + 90;
    }
    @Override
    public void loop() {
        if ((getRuntime() > endGameTime) && !inEndGame) {
            gamepad1.rumbleBlips(3);
            inEndGame = true;
        }
        telemetry.addData("Time:", getRuntime());
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.right_stick_x;
        double sx = gamepad1.left_stick_x;
        board.runDriveForward(y, sx, x);
    }
}
