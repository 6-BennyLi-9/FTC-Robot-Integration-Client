package org.firstinspires.ftc.teamcode.drives.controls.definition;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.hardwares.Chassis;

import java.util.LinkedList;

public interface DriverProgram {
	void update();
	default void runOrderPackage(@NonNull DriveOrderPackage orderPackage){}
	default void runOrderPackage(@NonNull LinkedList<DriveOrder> orders){}
	Chassis getClassic();
	Pose2d getCurrentPose();
}
