package org.firstinspires.ftc.masters.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(15)
            .lateralZeroPowerAcceleration(-85.01)
            .forwardZeroPowerAcceleration(-50.7)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.25, 0, 0.01, 0))
            .headingPIDFCoefficients(new PIDFCoefficients(2.5, 0, 0.00001, 0))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.02, 0.0, 0.00001, 0.6, 0))
            .centripetalScaling(0.00095);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRight")
            .rightRearMotorName("backRight")
            .leftRearMotorName("backLeft")
            .leftFrontMotorName("frontLeft")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(69.1)
            .yVelocity(50.76);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 0.9, 1);

    public static PinpointConstants localizerConstants = new PinpointConstants()

            .forwardPodY(7.1)
            .strafePodX(2)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);


    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .mecanumDrivetrain(driveConstants)
                .pathConstraints(pathConstraints)
                .build();
    }

//        public static Follower createFollower(HardwareMap hardwareMap) {
//        return new FollowerBuilder(followerConstants, hardwareMap)
//                .twoWheelLocalizer(localizerConstants)
//                .mecanumDrivetrain(driveConstants)
//                .pathConstraints(pathConstraints)
//                .build();
//    }

//public static TwoWheelConstants localizerConstants= new TwoWheelConstants()
//        .forwardEncoder_HardwareMapName("backLeft")
//        .strafeEncoder_HardwareMapName("backRight")
//            .strafePodX(4.2)
//            .forwardPodY(-7.5)
//        .IMU_HardwareMapName("imu")
//        .IMU_Orientation(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,RevHubOrientationOnRobot.UsbFacingDirection.FORWARD))
//        .forwardEncoderDirection(Encoder.REVERSE)
//            .strafeEncoderDirection(Encoder.FORWARD).
//        strafeTicksToInches(0.0020111)
//        ;
//
//}

}