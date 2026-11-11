package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.mechanisms.FirstRobotBoard;

@TeleOp
public class FirstOpmode2026 extends OpMode {
    FirstRobotBoard board = new FirstRobotBoard();
    boolean bWasPressed = false;
    @Override
    public void init() {
        board.init(hardwareMap);
    }
    @Override
    public void loop() {
        board.cartesianDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, (gamepad1.right_trigger * .8) - (gamepad1.left_trigger * .8));
        if (gamepad1.a) {
            board.extractFlowers(0.5);
        }
        else {
            board.extractFlowers(1.0);
        }
        if (gamepad1.b && !bWasPressed) {
            board.intakeControl(1.0);
            bWasPressed = !bWasPressed;
        }
        else {
            board.intakeControl(0.0);
            bWasPressed = !bWasPressed;
        }
    }
}
