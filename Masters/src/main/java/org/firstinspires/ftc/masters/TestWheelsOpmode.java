package org.firstinspires.ftc.masters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.masters.mechanisms.ProgrammingBoard1;

@TeleOp
public class TestWheelsOpmode extends OpMode {
    ProgrammingBoard1 board = new ProgrammingBoard1();
    @Override
    public void init() {
    board.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            board.powerFrontLeft(0.5);
        }
        else {
            board.powerFrontLeft(0.0);
        }
        if (gamepad1.b) {
            board.powerFrontRight(0.5);
        }
        else {
            board.powerFrontRight(0.0);
        }
        if (gamepad1.x) {
            board.powerBackLeft(0.5);
        }
        else {
            board.powerBackLeft(0.0);
        }
        if (gamepad1.y) {
            board.powerBackRight(0.5);
        }
        else {
            board.powerBackRight(0.0);
        }
    }
}
