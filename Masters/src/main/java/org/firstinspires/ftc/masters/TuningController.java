// https://github.com/NoahBres/VelocityPIDTuningTutorial

package org.firstinspires.ftc.masters;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Configurable
@Disabled
public class TuningController {
    public static double MOTOR_TICKS_PER_REV = 28;
    public static double MOTOR_MAX_RPM = 6000;
    public static double MOTOR_GEAR_RATIO = 1; // output (wheel) speed / input (motor) speed

    public static double TESTING_MAX_SPEED = 0.9 * MOTOR_MAX_RPM;
    public static double TESTING_MIN_SPEED = 0.3 * MOTOR_MAX_RPM;


    // These are prefixed with "STATE1", "STATE2", etc. because Dashboard displays variables in
    // alphabetical order. Thus, we preserve the actual order of the process
    // Then we append Z just because we want it to show below the MOTOR_ and TESTING_ because
    // these settings aren't as important
    public static double ZSTATE1_RAMPING_UP_DURATION = 3.5;
    public static double ZSTATE2_COASTING_1_DURATION = 4;
    public static double ZSTATE3_RAMPING_DOWN_DURATION = 2;
    public static double ZSTATE4_COASTING_2_DURATION = 2;
    public static double ZSTATE5_RANDOM_1_DURATION = 2;
    public static double ZSTATE6_RANDOM_2_DURATION = 2;
    public static double ZSTATE7_RANDOM_3_DURATION = 2;
    public static double ZSTATE8_REST_DURATION = 1;

    enum State {
        RAMPING_UP,
        COASTING_1,
        RAMPING_DOWN,
        COASTING_2,
        RANDOM_1,
        RANDOM_2,
        RANDOM_3,
        REST
    }

    private State currentState = State.RAMPING_UP;
    private long stateStartTime = 0;
    private long stateTimer = 0;
    private double currentTargetVelo = 0.0;
    private double randomValue1, randomValue2, randomValue3;

    public TuningController() {
    }

    public void start() {
        currentState = State.RAMPING_UP;
        stateStartTime = System.currentTimeMillis();
        stateTimer = 0;
        generateRandomValues();
    }

    private void generateRandomValues() {
        randomValue1 = Math.random() * (TESTING_MAX_SPEED - TESTING_MIN_SPEED) + TESTING_MIN_SPEED;
        randomValue2 = Math.random() * (TESTING_MAX_SPEED - TESTING_MIN_SPEED) + TESTING_MIN_SPEED;
        randomValue3 = Math.random() * (TESTING_MAX_SPEED - TESTING_MIN_SPEED) + TESTING_MIN_SPEED;
    }

    public double update() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - stateStartTime) / 1000.0;

        switch (currentState) {
            case RAMPING_UP:
                if (elapsedTime < ZSTATE1_RAMPING_UP_DURATION) {
                    double progress = elapsedTime / ZSTATE1_RAMPING_UP_DURATION;
                    double target = progress * (TESTING_MAX_SPEED - TESTING_MIN_SPEED) + TESTING_MIN_SPEED;
                    currentTargetVelo = rpmToTicksPerSecond(target);
                } else {
                    currentState = State.COASTING_1;
                    stateStartTime = currentTime;
                    currentTargetVelo = rpmToTicksPerSecond(TESTING_MAX_SPEED);
                }
                break;

            case COASTING_1:
                if (elapsedTime >= ZSTATE2_COASTING_1_DURATION) {
                    currentState = State.RAMPING_DOWN;
                    stateStartTime = currentTime;
                }
                break;

            case RAMPING_DOWN:
                if (elapsedTime < ZSTATE3_RAMPING_DOWN_DURATION) {
                    double progress = elapsedTime / ZSTATE3_RAMPING_DOWN_DURATION;
                    double target = TESTING_MAX_SPEED - progress * (TESTING_MAX_SPEED - TESTING_MIN_SPEED);
                    currentTargetVelo = rpmToTicksPerSecond(target);
                } else {
                    currentState = State.COASTING_2;
                    stateStartTime = currentTime;
                    currentTargetVelo = rpmToTicksPerSecond(TESTING_MIN_SPEED);
                }
                break;

            case COASTING_2:
                if (elapsedTime >= ZSTATE4_COASTING_2_DURATION) {
                    currentState = State.RANDOM_1;
                    stateStartTime = currentTime;
                    currentTargetVelo = rpmToTicksPerSecond(randomValue1);
                }
                break;

            case RANDOM_1:
                if (elapsedTime >= ZSTATE5_RANDOM_1_DURATION) {
                    currentState = State.RANDOM_2;
                    stateStartTime = currentTime;
                    currentTargetVelo = rpmToTicksPerSecond(randomValue2);
                }
                break;

            case RANDOM_2:
                if (elapsedTime >= ZSTATE6_RANDOM_2_DURATION) {
                    currentState = State.RANDOM_3;
                    stateStartTime = currentTime;
                    currentTargetVelo = rpmToTicksPerSecond(randomValue3);
                }
                break;

            case RANDOM_3:
                if (elapsedTime >= ZSTATE7_RANDOM_3_DURATION) {
                    currentState = State.REST;
                    stateStartTime = currentTime;
                    currentTargetVelo = 0;
                }
                break;

            case REST:
                if (elapsedTime >= ZSTATE8_REST_DURATION) {
                    currentState = State.RAMPING_UP;
                    stateStartTime = currentTime;
                    generateRandomValues();
                }
                break;
        }

        return currentTargetVelo;
    }

    public static double rpmToTicksPerSecond(double rpm) {
        return rpm * MOTOR_TICKS_PER_REV / MOTOR_GEAR_RATIO / 60;
    }
}
