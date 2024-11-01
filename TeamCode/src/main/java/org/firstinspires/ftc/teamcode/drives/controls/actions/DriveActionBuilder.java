package org.firstinspires.ftc.teamcode.drives.controls.actions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.controls.MecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DriveActionBuilder implements DriveOrderBuilder {
	private final DriveActionPackage actionPackage;
	private final DriverProgram drive;
	private DriveAction cache;

	public DriveActionBuilder(@NonNull final MecanumDrive drive) {
		this.actionPackage = new DriveActionPackage();
		this.actionPackage.actions.add(new DriveAction(drive.chassis, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}
	DriveActionBuilder(final DriverProgram drive, final DriveActionPackage actionPackage) {
		this.actionPackage = actionPackage;
		this.drive = drive;
	}

	@Override
	public DriveOrderBuilder SetPower(double power) {
		power = Mathematics.intervalClip(power, - 1.0f, 1.0f);
		this.cache = new DriveAction(this.drive.getClassic(), this.actionPackage.actions.getLast().BufPower, this.actionPackage.actions.getLast().nextPose());
		this.cache.setPower(power);
		this.cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		this.actionPackage.actions.add(this.cache);
		return new DriveActionBuilder(this.drive, this.actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnRadians(double radians) {
		radians = Mathematics.intervalClip(radians, - Math.PI, Math.PI);
		this.cache = new DriveAction(this.drive.getClassic(), this.actionPackage.actions.getLast().BufPower, this.actionPackage.actions.getLast().nextPose());
		this.cache.turn(radians);
		this.cache.trajectoryType = TrajectoryType.TurnOnly;
		this.actionPackage.actions.add(this.cache);
		return new DriveActionBuilder(this.drive, this.actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnAngle(final double deg) {
		return this.TurnRadians(Math.toRadians(deg));
	}

	@Override
	public DriveOrderBuilder StrafeInDistance(final double radians, final double distance) {
		this.cache = new DriveAction(this.drive.getClassic(), this.actionPackage.actions.getLast().BufPower, this.actionPackage.actions.getLast().nextPose());
		this.cache.strafeInDistance(radians, distance);
		this.cache.trajectoryType = TrajectoryType.LinerStrafe;
		this.actionPackage.actions.add(this.cache);
		return new DriveActionBuilder(this.drive, this.actionPackage);
	}

	@Override
	public DriveOrderBuilder StrafeTo(final Vector2d pose) {
		this.cache = new DriveAction(this.drive.getClassic(), this.actionPackage.actions.getLast().BufPower, this.actionPackage.actions.getLast().nextPose());
		this.cache.strafeTo(pose);
		this.cache.trajectoryType = TrajectoryType.LinerStrafe;
		this.actionPackage.actions.add(this.cache);
		return new DriveActionBuilder(this.drive, this.actionPackage);
	}

	@Override
	public DriveOrderPackage END() {
		return this.actionPackage;
	}
}
