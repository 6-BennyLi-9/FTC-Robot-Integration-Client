package org.firstinspires.ftc.teamcode.DriveControls.Actions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.DriverProgram;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;

public class DrivingActionsBuilder {
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

}
