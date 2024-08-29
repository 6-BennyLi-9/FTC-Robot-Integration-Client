package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrder;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runCommandPackage(@NonNull DriveOrderPackage orderPackage){}
	default void runCommandPackage(@NonNull LinkedList<DriveOrder> orders){}
	Classic getClassic();
}
