package org.firstinspires.ftc.teamcode.drives.controls.actions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.controls.MecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DriveActionBuilder implements DriveOrderBuilder {
	private final DriveActionPackage actionPackage;
	private final DriverProgram drive;
	private DriveAction cache;

	public DriveActionBuilder(@NonNull MecanumDrive drive) {
		actionPackage = new DriveActionPackage();
		actionPackage.actions.add(new DriveAction(drive.chassis, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}
	DriveActionBuilder(DriverProgram drive, DriveActionPackage actionPackage) {
		this.actionPackage = actionPackage;
		this.drive = drive;
	}

	@Override
	public DriveOrderBuilder SetPower(double power) {
		power = Functions.intervalClip(power, -1f, 1f);
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().nextPose());
		cache.setPower(power);
		cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		actionPackage.actions.add(cache);
		return new DriveActionBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnRadians(double radians) {
		radians = Functions.intervalClip(radians, -Math.PI, Math.PI);
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().nextPose());
		cache.turn(radians);
		cache.trajectoryType = TrajectoryType.TurnOnly;
		actionPackage.actions.add(cache);
		return new DriveActionBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnAngle(double deg) {
		return TurnRadians(Math.toRadians(deg));
	}

	@Override
	public DriveOrderBuilder StrafeInDistance(double radians, double distance) {
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().nextPose());
		cache.strafeInDistance(radians, distance);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		actionPackage.actions.add(cache);
		return new DriveActionBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder StrafeTo(Vector2d pose) {
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().nextPose());
		cache.strafeTo(pose);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		actionPackage.actions.add(cache);
		return new DriveActionBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderPackage END() {
		return actionPackage;
	}
}
