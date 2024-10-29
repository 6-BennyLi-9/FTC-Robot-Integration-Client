package org.firstinspires.ftc.teamcode.drives.controls.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DrivingCommandsBuilder implements DriveOrderBuilder {
	private final DriveCommandPackage commandPackage;
	private final DriverProgram drive;
	private DriveCommand cache;

	public DrivingCommandsBuilder(@NonNull SimpleMecanumDrive drive) {
		commandPackage = new DriveCommandPackage();
		commandPackage.commands.add(new DriveCommand(drive.chassis, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}

	DrivingCommandsBuilder(DriverProgram drive, DriveCommandPackage commandPackage) {
		this.commandPackage = commandPackage;
		this.drive = drive;
	}

	public DrivingCommandsBuilder SetPower(double power) {
		power = Functions.intervalClip(power, -1f, 1f);
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().nextPose());
		cache.setPower(power);
		cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder TurnRadians(double radians) {
		radians = Functions.intervalClip(radians, -Math.PI, Math.PI);
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().nextPose());
		cache.turn(radians);
		cache.trajectoryType = TrajectoryType.TurnOnly;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder TurnAngle(double deg) {
		return TurnRadians(Math.toRadians(deg));
	}

	public DrivingCommandsBuilder StrafeInDistance(double radians, double distance) {
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().nextPose());
		cache.strafeInDistance(radians, distance);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder StrafeTo(Vector2d pose) {
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().nextPose());
		cache.strafeTo(pose);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DriveCommandPackage END() {
		return commandPackage;
	}
}
