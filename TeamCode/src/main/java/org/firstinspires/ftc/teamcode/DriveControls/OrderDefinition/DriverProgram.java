package org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runOrderPackage(@NonNull DriveOrderPackage orderPackage){}
	default void runOrderPackage(@NonNull LinkedList<DriveOrder> orders){}
	Classic getClassic();
	Pose2d getCurrentPose();
}
