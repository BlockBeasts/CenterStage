package org.firstinspires.ftc.masters.world.paths;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.masters.drive.SampleMecanumDrive;
import org.firstinspires.ftc.masters.trajectorySequence.TrajectorySequence;

public class RedFarSidePath {

    public static TrajectorySequence getRightPurple(SampleMecanumDrive drive, Pose2d startPose) {

            return drive.trajectorySequenceBuilder(startPose)
                    .setTangent(Math.toRadians(150))
                    .splineToLinearHeading(new Pose2d(-38, -32, Math.toRadians(-180)), Math.toRadians(30))
                    .build();
    }
    public static TrajectorySequence getLeftPurple (SampleMecanumDrive drive,  Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(70))
                .splineToLinearHeading(new Pose2d(-47.5, -15, Math.toRadians(90)), Math.toRadians(130))
                .build();
    }

    public static TrajectorySequence getMidPurple(SampleMecanumDrive drive,  Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-47, -11, Math.toRadians(130)), Math.toRadians(90))
                .build();
    }

    public static TrajectorySequence getRightPurpleToStack(SampleMecanumDrive drive, Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-57, -11.8, Math.toRadians(180)), Math.toRadians(180))
                .build();
    }

    public static TrajectorySequence getLeftPurpleToStack(SampleMecanumDrive drive, Pose2d startPose) {

        return  drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-57, -11.8, Math.toRadians(180)), Math.toRadians(180))
                .build();
    }

    public static TrajectorySequence getMidPurpleToStack(SampleMecanumDrive drive, Pose2d startPose) {

        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-57, -12, Math.toRadians(180)), Math.toRadians(180))
                .build();
    }

    public static TrajectorySequence getStackToRightYellow(SampleMecanumDrive drive, Pose2d startPose) {

        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(30, -11.5, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(50, -36, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

    public static TrajectorySequence getStackToMidYellow(SampleMecanumDrive drive, Pose2d startPose) {

        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(20, -11.5, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(50, -31.5, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

    public static TrajectorySequence getStackToLeftYellow(SampleMecanumDrive drive, Pose2d startPose) {

        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(30, -11.5, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(50, -29, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

    public static TrajectorySequence getStackToLeftYellowWMI(SampleMecanumDrive drive, Pose2d startPose) {

        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(25, -11.5, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(40, -29, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

    public static TrajectorySequence toStackFromBackboardGate (SampleMecanumDrive drive, Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(10, -11.5, Math.toRadians(180)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-57, -11.5, Math.toRadians(180)), Math.toRadians(180))
                .build();
    }

    public static TrajectorySequence toBackboardGate(SampleMecanumDrive drive, Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(10, -11.5, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(47, -28, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

    public static TrajectorySequence park(SampleMecanumDrive drive, Pose2d startPose){
        return drive.trajectorySequenceBuilder(startPose)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(44, -15, Math.toRadians(180)), Math.toRadians(0))
                .build();
    }

}
