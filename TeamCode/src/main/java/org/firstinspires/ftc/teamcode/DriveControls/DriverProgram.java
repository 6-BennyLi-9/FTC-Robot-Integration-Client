package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runCommandPackage(@NonNull DriveCommandPackage commandPackage){}
	default void runCommandPackage(@NonNull LinkedList<DriveCommand> commands){}
}
