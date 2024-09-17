package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;

public interface Odometry {
	/**
	 * @param relDeltaTheta 默认0°为X轴正方向
	 */
	@UtilFunctions
	void update(double relDeltaX, double relDeltaY, double relDeltaTheta);
	@UtilFunctions
	default void registerLineToDashBoard(){}
	@UtilFunctions
	void registerRobotToDashBoard();
	@UtilFunctions
	Pose2d getCurrentPose();

	@UserRequirementFunctions
	void setColor(String color);
}
