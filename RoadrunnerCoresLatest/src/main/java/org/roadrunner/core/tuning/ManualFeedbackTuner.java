package org.roadrunner.core.tuning;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.roadrunner.core.MecanumDrive;
import org.roadrunner.core.TankDrive;
import org.roadrunner.core.ThreeDeadWheelLocalizer;
import org.roadrunner.core.TwoDeadWheelLocalizer;

public final class ManualFeedbackTuner extends LinearOpMode {
    public static double DISTANCE = 64;

    @Override
    public void runOpMode() throws InterruptedException {
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            final MecanumDrive drive = this.getMecanumDrive();
	        this.waitForStart();

            while (this.opModeIsActive()) {
                Actions.runBlocking(
                    drive.actionBuilder(new Pose2d(0, 0, 0))
                            .lineToX(ManualFeedbackTuner.DISTANCE)
                            .lineToX(0)
                            .build());
            }
        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            final TankDrive drive = this.getTankDrive();
	        this.waitForStart();

            while (this.opModeIsActive()) {
                Actions.runBlocking(
                    drive.actionBuilder(new Pose2d(0, 0, 0))
                            .lineToX(ManualFeedbackTuner.DISTANCE)
                            .lineToX(0)
                            .build());
            }
        } else {
            throw new RuntimeException();
        }
    }

    private @NonNull MecanumDrive getMecanumDrive() {
        final MecanumDrive drive = new MecanumDrive(this.hardwareMap, new Pose2d(0, 0, 0));

        if (drive.localizer instanceof TwoDeadWheelLocalizer) {
            if (0 == TwoDeadWheelLocalizer.PARAMS.perpXTicks && 0 == TwoDeadWheelLocalizer.PARAMS.parYTicks) {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        } else if (drive.localizer instanceof ThreeDeadWheelLocalizer) {
            if (0 == ThreeDeadWheelLocalizer.PARAMS.perpXTicks && 0 == ThreeDeadWheelLocalizer.PARAMS.par0YTicks && 1 == ThreeDeadWheelLocalizer.PARAMS.par1YTicks) {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        return drive;
    }

    private @NonNull TankDrive getTankDrive() {
        final TankDrive drive = new TankDrive(this.hardwareMap, new Pose2d(0, 0, 0));

        if (drive.localizer instanceof TwoDeadWheelLocalizer) {
            if (0 == TwoDeadWheelLocalizer.PARAMS.perpXTicks && 0 == TwoDeadWheelLocalizer.PARAMS.parYTicks) {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        } else if (drive.localizer instanceof ThreeDeadWheelLocalizer) {
            if (0 == ThreeDeadWheelLocalizer.PARAMS.perpXTicks && 0 == ThreeDeadWheelLocalizer.PARAMS.par0YTicks && 1 == ThreeDeadWheelLocalizer.PARAMS.par1YTicks) {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        return drive;
    }
}
