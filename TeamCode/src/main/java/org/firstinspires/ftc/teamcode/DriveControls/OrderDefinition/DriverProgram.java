package org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runCommandPackage(@NonNull DriveOrderPackage orderPackage){}
	default void runCommandPackage(@NonNull LinkedList<DriveOrder> orders){}
	Classic getClassic();
}
