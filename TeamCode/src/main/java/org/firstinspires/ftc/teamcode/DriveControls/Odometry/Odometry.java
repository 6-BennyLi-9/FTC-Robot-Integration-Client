package org.firstinspires.ftc.teamcode.DriveControls.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

public interface Odometry {
	void ProcessDeltaRelPose(double relX,double relY,double relTheta);
	default void RegisterToDashBoard(){}
	Pose2d getCurrentPose();
}
