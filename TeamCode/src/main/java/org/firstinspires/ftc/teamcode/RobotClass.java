//package org.firstinspires.ftc.masters;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorImplEx;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//
//import java.util.Date;
//
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.imgproc.Imgproc;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvInternalCamera;
//import org.openftc.easyopencv.OpenCvPipeline;
//
//
//public class RobotClass {
//    public DcMotor frontLeft;
//    public DcMotor frontRight;
//    public DcMotor backLeft;
//    public DcMotor backRight;
//    private double ticks = 537;//537
//    private double ticksTheSequel = 2786;
//    BNO055IMU imu;
//
//    public Telemetry telemetry;
//    ColorSensor leftColorSensor;
//    ColorSensor rightColorSensor;
//    ColorSensor frontColorSensor;
//
//    OpenCvInternalCamera phoneCam;
//    EasyOpenCVExample.SkystoneDeterminationPipeline pipeline;
//
//    LinearOpMode opmode;
//    HardwareMap hardwareMap;
//    String color;
//
//    static Point REGION1_TOPLEFT_ANCHOR_POINT;
//
//
//    public RobotClass(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opmode, String color) {
//        this.hardwareMap= hardwareMap;
//        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft" );
//        frontRight = hardwareMap.get(DcMotor.class, "frontRight" );
//        backLeft = hardwareMap.get(DcMotor.class, "backLeft" );
//        backRight = hardwareMap.get(DcMotor.class, "backRight" );
////        colorSensor = hardwareMap.colorSensor.get("colorSensor");
//
//        motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        frontLeft.setDirection(DcMotor.Direction.REVERSE);
//        frontRight.setDirection(DcMotor.Direction.FORWARD);
//        backLeft.setDirection(DcMotor.Direction.REVERSE);
//        backRight.setDirection(DcMotor.Direction.FORWARD);
//
//        this.telemetry = telemetry;
//
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        // parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = true;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm=null;//= new JustLoggingAccelerationIntegrator();
//
//        imu = hardwareMap.get(BNO055IMU.class, "imu1");
//        imu.initialize(parameters);
//
//        this.opmode = opmode;
//        REGION1_TOPLEFT_ANCHOR_POINT = new Point(192,176);
//        this.color= color;
//
//    }
//
//    public RobotClass(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opmode) {
//        this.hardwareMap= hardwareMap;
//        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft" );
//        frontRight = hardwareMap.get(DcMotor.class, "frontRight" );
//        backLeft = hardwareMap.get(DcMotor.class, "backLeft" );
//        backRight = hardwareMap.get(DcMotor.class, "backRight" );
//
//
//        motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        frontLeft.setDirection(DcMotor.Direction.REVERSE);
//        frontRight.setDirection(DcMotor.Direction.FORWARD);
//        backLeft.setDirection(DcMotor.Direction.REVERSE);
//        backRight.setDirection(DcMotor.Direction.FORWARD);
//
//        this.telemetry = telemetry;
//
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        // parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = true;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm=null;//= new JustLoggingAccelerationIntegrator();
//
//        imu = hardwareMap.get(BNO055IMU.class, "imu1");
//        imu.initialize(parameters);
//
//        this.opmode = opmode;
//        REGION1_TOPLEFT_ANCHOR_POINT = new Point(192,176);
//        this.color= "blue";
//
//    }
//
//    public RingPosition analyze() {
//        pipeline.getAnalysis();
//        return pipeline.position;
//    }
//
//    public void testGyro(){
//        while(opmode.opModeIsActive()){
//            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
//            telemetry.addData("gravity",imu.getGravity().toString());
//            telemetry.addData("1",angles.firstAngle);
//            telemetry.addData("2", angles.secondAngle);
//            telemetry.addData("3", angles.thirdAngle);
//            telemetry.update();
//        }
//    }
//
//    public double getAngleFromGyro() {
//
//        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//
//        return angles.firstAngle;
//    }
//
//    public void forward (double speed, double rotations){
//        int leftCurrent = frontLeft.getCurrentPosition();
//        int rightCurrent = frontRight.getCurrentPosition();
//        int backLeftCurrent = backLeft.getCurrentPosition();
//        int backRightCurrent = backRight.getCurrentPosition();
//
//        telemetry.addData("Target Front Left Motor Position", leftCurrent);
//        telemetry.addData("Target Front Right Motor Position", rightCurrent);
//        telemetry.addData("Target Back Left Motor Position", backLeftCurrent);
//        telemetry.addData("Target Back Right Motor Position", backRightCurrent);
//        telemetry.update();
//
//
//        double toPositionLeft = leftCurrent + rotations*ticks;
//        double toPositionRight = rightCurrent + rotations*ticks;
//        double toPositionbackLeft = backLeftCurrent + rotations*ticks;
//        double toPositionbackRight = backRightCurrent + rotations*ticks;
//
//        telemetry.addData("Target Front Left Motor Position", toPositionLeft);
//        telemetry.addData("Target Front Right Motor Position", toPositionRight);
//        telemetry.addData("Target Back Left Motor Position", toPositionbackLeft);
//        telemetry.addData("Target Front Left Motor Position", toPositionbackLeft);
//        telemetry.update();
//
//        frontLeft.setTargetPosition((int)toPositionLeft);
//        frontRight.setTargetPosition((int)toPositionRight);
//        backLeft.setTargetPosition((int)toPositionbackLeft);
//        backRight.setTargetPosition((int)toPositionbackRight);
//
//        motorSetMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        frontLeft.setPower(Math.abs(speed));
//        frontRight.setPower(Math.abs(speed));
//        backLeft.setPower(Math.abs(speed));
//        backRight.setPower(Math.abs(speed));
//
//        while (this.opmode.opModeIsActive() &&
//                (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
//
//            // Display it for the driver.
//            motorTelemetry();
//        }
//        stopMotors();
//
//        motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//    }
//    public void backwards (double speed, double rotations) {
//        int leftCurrent = frontLeft.getCurrentPosition();
//        int rightCurrent = frontRight.getCurrentPosition();
//        int backLeftCurrent = backLeft.getCurrentPosition();
//        int backRightCurrent = backRight.getCurrentPosition();
//
//        double toPositionLeft = leftCurrent - rotations * ticks;
//        double toPositionRight = rightCurrent - rotations * ticks;
//        double toPositionbackLeft = backLeftCurrent - rotations * ticks;
//        double toPositionbackRight = backRightCurrent - rotations * ticks;
//
//        frontLeft.setTargetPosition((int) toPositionLeft);
//        frontRight.setTargetPosition((int) toPositionRight);
//        backLeft.setTargetPosition((int) toPositionbackLeft);
//        backRight.setTargetPosition((int) toPositionbackRight);
//
//        motorSetMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        frontLeft.setPower(speed);
//        frontRight.setPower(speed);
//        backLeft.setPower(speed);
//        backRight.setPower(speed);
//    }
//    public void setSpeedForTurnRight (double speed) {
//        frontLeft.setPower(speed);
//        frontRight.setPower(-speed);
//        backLeft.setPower(speed);
//        backRight.setPower(-speed);
//    }
//
//    public void setSpeedForTurnLeft (double speed) {
//        frontLeft.setPower(-speed);
//        frontRight.setPower(speed);
//        backLeft.setPower(-speed);
//        backRight.setPower(speed);
//    }
//
//    public void stopMotors () {
//        frontLeft.setPower(0);
//        frontRight.setPower(0);
//        backLeft.setPower(0);
//        backRight.setPower(0);
//    }
//
//    protected void motorTelemetry(){
//        telemetry.addData("Target Front Left Motor Position", frontLeft.getCurrentPosition());
//        telemetry.addData("Target Front Right Motor Position", frontRight.getCurrentPosition());
//        telemetry.addData("Target Back Left Motor Position", backLeft.getCurrentPosition());
//        telemetry.addData("Target Back Right Motor Position", backRight.getCurrentPosition());
//        telemetry.update();
//    }
//
//
//    public void pivotRightSloppy (double speed, double angle) {
//        setSpeedForTurnRight(speed);
//
//        double targetAngle = getAngleFromGyro() - angle;
//
//        while (getAngleFromGyro() > targetAngle && opmode.opModeIsActive()) {
//            telemetry.addData("Gyro Angle: ", getAngleFromGyro());
//            telemetry.update();
//        }
//
//        stopMotors();
//
//        telemetry.addData("Gyro Angle", getAngleFromGyro());
//        telemetry.update();
//    }
//
//    public void pivotLeftSloppy (double speed, double angle) {
//        setSpeedForTurnLeft(speed);
//
//        double targetAngle = getAngleFromGyro() + angle;
//
//        while (getAngleFromGyro() < targetAngle && opmode.opModeIsActive()) {
//            telemetry.addData("Gyro Angle: ", getAngleFromGyro());
//            telemetry.update();
//        }
//
//        stopMotors();
//
//        telemetry.addData("Gyro Angle", getAngleFromGyro());
//        telemetry.update();
//    }
//
//    public void pivotRight (double speed, double angle) {
//        double targetAngle = getAngleFromGyro() - angle;
//        pivotRightSloppy(speed, angle);
//
//        telemetry.addData("Middle Gyro Angle: ", getAngleFromGyro());
//        telemetry.update();
//
//        speed= speed*0.5;
//        if (getAngleFromGyro()<targetAngle-0.5) {
//            setSpeedForTurnLeft(speed);
//            while (getAngleFromGyro() < targetAngle && opmode.opModeIsActive()) {
//                telemetry.addData("Gyro Angle: ", getAngleFromGyro());
//                telemetry.update();
//            }
//        }
//
//        stopMotors();
//        telemetry.addData("Completed Gyro Angle: ", getAngleFromGyro());
//        telemetry.update();
//    }
//    public void pivotLeft (double speed, double angle) {
//        double targetAngle = getAngleFromGyro() + angle;
//        pivotLeftSloppy(speed, angle);
//
//        telemetry.addData("Middle Gyro Angle: ", getAngleFromGyro());
//        telemetry.update();
//
//        speed= speed*0.5;
//        setSpeedForTurnRight(speed);
//        if (getAngleFromGyro()>targetAngle+0.5) {
//            while (getAngleFromGyro() > targetAngle + 0.5 && opmode.opModeIsActive()) {
//                telemetry.addData("Gyro Angle: ", getAngleFromGyro());
//                telemetry.update();
//            }
//        }
//
//        stopMotors();
//        telemetry.addData("Completed Gyro Angle: ", getAngleFromGyro());
//        telemetry.update();
//    }
//    public void forwardToWhite (double speed, double rotations, double speed2) {
//        forward(speed,rotations);
//        frontLeft.setPower(speed2);
//        frontRight.setPower(speed2);
//        backLeft.setPower(speed2);
//        backRight.setPower(speed2);
//
////        while (colorSensor.alpha() < 600) {
////
////            telemetry.addData("Light Level: ", colorSensor.alpha());
////            telemetry.update();
////        }
//
//        stopMotors();
//    }
//    public void forwardToBlue (double speed, double rotations, double speed2) {
//        forward(speed,rotations);
//        frontLeft.setPower(speed2);
//        frontRight.setPower(speed2);
//        backLeft.setPower(speed2);
//        backRight.setPower(speed2);
//
////        while (colorSensor.blue() < 20) {
////
//////            telemetry.addData("Blue Level: ", colorSensor.blue());
////            telemetry.update();
////        }
//
//        stopMotors();
//    }
//    public void forwardToRed (double speed, double rotations, double speed2) throws InterruptedException {
//        forward(speed,rotations);
//        frontLeft.setPower(speed2);
//        frontRight.setPower(speed2);
//        backLeft.setPower(speed2);
//        backRight.setPower(speed2);
////
////        while (colorSensor.red() < 20) {
////
////            telemetry.addData("Red Level: ", colorSensor.red());
////            telemetry.update();
////        }
//
//        stopMotors();
//    }
//    public void mecanumWitchcraft (double degree, double time) {
//        double x = java.lang.Math.cos(degree);
//        double y = java.lang.Math.sin(degree);
//
//        frontLeft.setPower(y + x);
//        backLeft.setPower(y - x);
//        frontRight.setPower(y - x);
//        backRight.setPower(y + x);
//
//        pauseButInSecondsForThePlebeians(time);
//        stopMotors();
//
//    }
//    public void mecanumWitchcraftColor (double degree, double rotations, int color) throws InterruptedException {
//        double x = java.lang.Math.cos(degree);
//        double y = java.lang.Math.sin(degree);
//
//        frontLeft.setPower(y + x);
//        backLeft.setPower(y - x);
//        frontRight.setPower(y - x);
//        backRight.setPower(y + x);
//
////        if (color == 1) {
////            while (colorSensor.alpha() < 20) {
////
////            }
////        } else if (color == 2)  {
////            while (colorSensor.red() < 20) {
////
////            }
////        } else {
////            while (colorSensor.blue() < 20) {
////
////            }
////        }
////        stopMotors();
////    }
//    public void strafeLeft (double speed, double rotations) {
//
//        int leftCurrent = frontLeft.getCurrentPosition();
//        int rightCurrent = frontRight.getCurrentPosition();
//        int backLeftCurrent = backLeft.getCurrentPosition();
//        int backRightCurrent = backRight.getCurrentPosition();
//
//        double toPositionLeft = leftCurrent - rotations*ticks;
//        double toPositionRight = rightCurrent + rotations*ticks;
//        double toPositionbackLeft = backLeftCurrent + rotations*ticks;
//        double toPositionbackRight = backRightCurrent - rotations*ticks;
//
//        frontLeft.setTargetPosition((int)toPositionLeft);
//        frontRight.setTargetPosition((int)toPositionRight);
//        backLeft.setTargetPosition((int)toPositionbackLeft);
//        backRight.setTargetPosition((int)toPositionbackRight);
//
//        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        frontLeft.setPower(Math.abs(-speed));
//        frontRight.setPower(Math.abs(speed));
//        backLeft.setPower(Math.abs(speed));
//        backRight.setPower(Math.abs(-speed));
//        while (this.opmode.opModeIsActive() &&
//                (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
//
//            // Display it for the driver.
//            motorTelemetry();
//        }
//        stopMotors();
//
//        motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//    }
//    public void strafeRight (double speed, double rotations) {
//
//        int leftCurrent = frontLeft.getCurrentPosition();
//        int rightCurrent = frontRight.getCurrentPosition();
//        int backLeftCurrent = backLeft.getCurrentPosition();
//        int backRightCurrent = backRight.getCurrentPosition();
//
//        double toPositionLeft = leftCurrent + rotations*ticks;
//        double toPositionRight = rightCurrent - rotations*ticks;
//        double toPositionbackLeft = backLeftCurrent - rotations*ticks;
//        double toPositionbackRight = backRightCurrent + rotations*ticks;
//
//        frontLeft.setTargetPosition((int)toPositionLeft);
//        frontRight.setTargetPosition((int)toPositionRight);
//        backLeft.setTargetPosition((int)toPositionbackLeft);
//        backRight.setTargetPosition((int)toPositionbackRight);
//
//        motorSetMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        frontLeft.setPower(Math.abs(speed));
//        frontRight.setPower(Math.abs(-speed));
//        backLeft.setPower(Math.abs(-speed));
//        backRight.setPower(Math.abs(speed));
//        while (this.opmode.opModeIsActive() &&
//                (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
//
//            // Display it for the driver.
//            motorTelemetry();
//        }
//        stopMotors();
//
//        motorSetMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    protected void motorSetMode(DcMotor.RunMode runMode){
//        frontLeft.setMode(runMode);
//        frontRight.setMode(runMode);
//        backLeft.setMode(runMode);
//        backRight.setMode(runMode);
//    }
//
//    public void pause(int millis){
//        long startTime = new Date().getTime();
//        long time = 0;
//
//        while (time<millis && opmode.opModeIsActive()) {
//            time = new Date().getTime() - startTime;
//        }
//    }
//
//    public void pauseButInSecondsForThePlebeians(double seconds) {
//        long startTime = new Date().getTime();
//        long time = 0;
//
//        while (time<seconds*1000 && opmode.opModeIsActive()) {
//            time = new Date().getTime() - startTime;
//        }
//    }
//
//    public void openCVInnitShenanigans() {
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, cameraMonitorViewId);
//        pipeline = new SkystoneDeterminationPipeline(color);
//        phoneCam.setPipeline(pipeline);
//
//
//        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
//        // out when the RC activity is in portrait. We do our actual image processing assuming
//        // landscape orientation, though.
//        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
//
//        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
//        {
//            @Override
//            public void onOpened()
//            {
//                phoneCam.startStreaming(320,240, OpenCvCameraRotation.UPSIDE_DOWN);
//            }
//        });
//
//    }
//
//    public enum RingPosition
//    {
//        FOUR,
//        ONE,
//        NONE
//    }
//    public static class SkystoneDeterminationPipeline extends OpenCvPipeline
//    {
//
//        public SkystoneDeterminationPipeline(String color){
//            if ("red".equalsIgnoreCase(color)){
//                int FOUR_RING_THRESHOLD = 150;
//                int ONE_RING_THRESHOLD = 140;
//            }
//        }
//        /*
//         * An enum to define the skystone position
//         */
//
//
//        /*
//         * Some color constants
//         */
//        static final Scalar BLUE = new Scalar(0, 0, 255);
//        static final Scalar GREEN = new Scalar(0, 255, 0);
//
//        /*
//         * The core values which define the location and size of the sample regions
//         */
//
////inner blue 68,176 size 30,42
//        //outer blue 192, 176 size 30,42
//        //inside red, the same as outer blue
//        //outer red, the same as inside blue
//
//
//        static final int REGION_WIDTH = 30;
//        static final int REGION_HEIGHT = 42;
//
//        int FOUR_RING_THRESHOLD = 150;
//        int ONE_RING_THRESHOLD = 135;
//
//        Point region1_pointA = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x,
//                REGION1_TOPLEFT_ANCHOR_POINT.y);
//        Point region1_pointB = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
//                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
//
//        /*
//         * Working variables
//         */
//        Mat region1_Cb;
//        Mat YCrCb = new Mat();
//        Mat Cb = new Mat();
//        int avg1;
//
//        // Volatile since accessed by OpMode thread w/o synchronization
//        public volatile RingPosition position = RingPosition.FOUR;
//
//        /*
//         * This function takes the RGB frame, converts to YCrCb,
//         * and extracts the Cb channel to the 'Cb' variable
//         */
//        void inputToCb(Mat input)
//        {
//            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
//            Core.extractChannel(YCrCb, Cb, 1);
//        }
//
//        @Override
//        public void init(Mat firstFrame)
//        {
//            inputToCb(firstFrame);
//
//            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
//        }
//
//        @Override
//        public Mat processFrame(Mat input)
//        {
//            inputToCb(input);
//
//            avg1 = (int) Core.mean(region1_Cb).val[0];
//
//            Imgproc.rectangle(
//                    input, // Buffer to draw on
//                    region1_pointA, // First point which defines the rectangle
//                    region1_pointB, // Second point which defines the rectangle
//                    BLUE, // The color the rectangle is drawn in
//                    2); // Thickness of the rectangle lines
//
//            position = RingPosition.FOUR; // Record our analysis
//            if(avg1 > FOUR_RING_THRESHOLD){
//                position = RingPosition.FOUR;
//            }else if (avg1 > ONE_RING_THRESHOLD){
//                position = RingPosition.ONE;
//            }else{
//                position = RingPosition.NONE;
//            }
//
//            Imgproc.rectangle(
//                    input, // Buffer to draw on
//                    region1_pointA, // First point which defines the rectangle
//                    region1_pointB, // Second point which defines the rectangle
//                    GREEN, // The color the rectangle is drawn in
//                    -1); // Negative thickness means solid fill
//
//            return input;
//        }
//
//
//        public int getAnalysis()
//        {
//            return avg1;
//        }
//    }
//}
