package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;

public interface Odometry {
	/**
	 * @param relTheta 默认0°为X轴正方向
	 */
	@UtilFunctions
	void ProcessDeltaRelPose(double relX,double relY,double relTheta);
	@UtilFunctions
	default void RegisterToDashBoard(){}
	@UtilFunctions
	Pose2d getCurrentPose();
}
