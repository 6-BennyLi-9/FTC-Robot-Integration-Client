package org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runOrderPackage(@NonNull DriveOrderPackage orderPackage){}
	default void runOrderPackage(@NonNull LinkedList<DriveOrder> orders){}
	Classic getClassic();
}
