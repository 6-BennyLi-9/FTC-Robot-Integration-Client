package org.firstinspires.ftc.teamcode.drives.controls.definition;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.utils.Position2d;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runOrderPackage(@NonNull final DriveOrderPackage orderPackage){}
	default void runOrderPackage(@NonNull final LinkedList<DriveOrder> orders){}
	Chassis getClassic();
	Position2d getCurrentPose();
}
