package org.firstinspires.ftc.teamcode.drives.controls.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DrivingCommandsBuilder implements DriveOrderBuilder {
	private final DriveCommandPackage commandPackage;
	private final DriverProgram drive;
	private DriveCommand cache;

	public DrivingCommandsBuilder(@NonNull final SimpleMecanumDrive drive) {
		this.commandPackage = new DriveCommandPackage();
		this.commandPackage.commands.add(new DriveCommand(drive.chassis, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}

	DrivingCommandsBuilder(final DriverProgram drive, final DriveCommandPackage commandPackage) {
		this.commandPackage = commandPackage;
		this.drive = drive;
	}

	public DrivingCommandsBuilder SetPower(double power) {
		power = Mathematics.intervalClip(power, - 1.0f, 1.0f);
		this.cache = new DriveCommand(this.drive.getClassic(), this.commandPackage.commands.getLast().BufPower, this.commandPackage.commands.getLast().nextPose());
		this.cache.setPower(power);
		this.cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		this.commandPackage.commands.add(this.cache);
		return new DrivingCommandsBuilder(this.drive, this.commandPackage);
	}

	public DrivingCommandsBuilder TurnRadians(double radians) {
		radians = Mathematics.intervalClip(radians, - Math.PI, Math.PI);
		this.cache = new DriveCommand(this.drive.getClassic(), this.commandPackage.commands.getLast().BufPower, this.commandPackage.commands.getLast().nextPose());
		this.cache.turn(radians);
		this.cache.trajectoryType = TrajectoryType.TurnOnly;
		this.commandPackage.commands.add(this.cache);
		return new DrivingCommandsBuilder(this.drive, this.commandPackage);
	}

	public DrivingCommandsBuilder TurnAngle(final double deg) {
		return this.TurnRadians(Math.toRadians(deg));
	}

	public DrivingCommandsBuilder StrafeInDistance(final double radians, final double distance) {
		this.cache = new DriveCommand(this.drive.getClassic(), this.commandPackage.commands.getLast().BufPower, this.commandPackage.commands.getLast().nextPose());
		this.cache.strafeInDistance(radians, distance);
		this.cache.trajectoryType = TrajectoryType.LinerStrafe;
		this.commandPackage.commands.add(this.cache);
		return new DrivingCommandsBuilder(this.drive, this.commandPackage);
	}

	public DrivingCommandsBuilder StrafeTo(final Vector2d pose) {
		this.cache = new DriveCommand(this.drive.getClassic(), this.commandPackage.commands.getLast().BufPower, this.commandPackage.commands.getLast().nextPose());
		this.cache.strafeTo(pose);
		this.cache.trajectoryType = TrajectoryType.LinerStrafe;
		this.commandPackage.commands.add(this.cache);
		return new DrivingCommandsBuilder(this.drive, this.commandPackage);
	}

	public DriveCommandPackage END() {
		return this.commandPackage;
	}
}
