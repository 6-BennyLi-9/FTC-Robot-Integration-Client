package org.firstinspires.ftc.teamcode.DriveControls.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriverProgram;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.Enums.TrajectoryType;
import org.firstinspires.ftc.teamcode.Utils.Mathematics;

public class DrivingActionsBuilder implements DriveOrderBuilder {
	private final DriveActionPackage actionPackage;
	private final DriverProgram drive;
	private DriveAction cache;

	public DrivingActionsBuilder(@NonNull SimpleMecanumDrive drive) {
		actionPackage = new DriveActionPackage();
		actionPackage.actions.add(new DriveAction(drive.classic, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}
	DrivingActionsBuilder(DriverProgram drive, DriveActionPackage actionPackage) {
		this.actionPackage = actionPackage;
		this.drive = drive;
	}

	@Override
	public DriveOrderBuilder SetPower(double power) {
		power = Mathematics.intervalClip(power, -1f, 1f);
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().NEXT());
		cache.SetPower(power);
		cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		actionPackage.actions.add(cache);
		return new DrivingActionsBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnRadians(double radians) {
		radians = Mathematics.intervalClip(radians, -Math.PI, Math.PI);
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().NEXT());
		cache.Turn(radians);
		cache.trajectoryType = TrajectoryType.TurnOnly;
		actionPackage.actions.add(cache);
		return new DrivingActionsBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder TurnAngle(double deg) {
		return TurnRadians(Math.toRadians(deg));
	}

	@Override
	public DriveOrderBuilder StrafeInDistance(double radians, double distance) {
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().NEXT());
		cache.StrafeInDistance(radians, distance);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		actionPackage.actions.add(cache);
		return new DrivingActionsBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderBuilder StrafeTo(Vector2d pose) {
		cache = new DriveAction(drive.getClassic(), actionPackage.actions.getLast().BufPower, actionPackage.actions.getLast().NEXT());
		cache.StrafeTo(pose);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		actionPackage.actions.add(cache);
		return new DrivingActionsBuilder(drive, actionPackage);
	}

	@Override
	public DriveOrderPackage END() {
		return actionPackage;
	}
}
