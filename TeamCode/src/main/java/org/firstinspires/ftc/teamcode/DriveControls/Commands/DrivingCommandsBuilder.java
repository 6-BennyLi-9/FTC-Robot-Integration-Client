package org.firstinspires.ftc.teamcode.DriveControls.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.DriveControls.DriverProgram;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.Enums.TrajectoryType;
import org.firstinspires.ftc.teamcode.Utils.Mathematics;

public class DrivingCommandsBuilder implements DriveOrderBuilder {
	private final DriveCommandPackage commandPackage;
	private final DriverProgram drive;
	private DriveCommand cache;

	public DrivingCommandsBuilder(@NonNull SimpleMecanumDrive drive) {
		commandPackage = new DriveCommandPackage();
		commandPackage.commands.add(new DriveCommand(drive.classic, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}

	DrivingCommandsBuilder(DriverProgram drive, DriveCommandPackage commandPackage) {
		this.commandPackage = commandPackage;
		this.drive = drive;
	}

	public DrivingCommandsBuilder SetPower(double power) {
		power = Mathematics.intervalClip(power, -1f, 1f);
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.SetPower(power);
		cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder TurnRadians(double radians) {
		radians = Mathematics.intervalClip(radians, -Math.PI, Math.PI);
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.Turn(radians);
		cache.trajectoryType = TrajectoryType.TurnOnly;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder TurnAngle(double deg) {
		return TurnRadians(Math.toRadians(deg));
	}

	public DrivingCommandsBuilder StrafeInDistance(double radians, double distance) {
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.StrafeInDistance(radians, distance);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DrivingCommandsBuilder StrafeTo(Vector2d pose) {
		cache = new DriveCommand(drive.getClassic(), commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.StrafeTo(pose);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new DrivingCommandsBuilder(drive, commandPackage);
	}

	public DriveCommandPackage END() {
		return commandPackage;
	}
}
