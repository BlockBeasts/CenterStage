package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.mechanisms.ProgrammingBoard2;

@TeleOp
public class MotorOpMode2 extends OpMode {
    double strafe = 0.25;
    ProgrammingBoard2 board = new ProgrammingBoard2();
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
        board.cartesianDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, (gamepad1.right_trigger * .8) - (gamepad1.left_trigger * .8));
    }
}
