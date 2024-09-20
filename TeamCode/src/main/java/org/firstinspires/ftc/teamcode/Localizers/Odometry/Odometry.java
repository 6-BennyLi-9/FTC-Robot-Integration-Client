package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;

public interface Odometry {
	/**
	 * @param relDeltaTheta 默认0°为X轴正方向，为角度制
	 */
	@UtilFunctions
	void update(double relDeltaX, double relDeltaY, double relDeltaTheta);
	@UtilFunctions
	default void registerLineToDashBoard(String tag){}
	@UtilFunctions
	void registerRobotToDashBoard(String tag);
	@UtilFunctions
	default void registerToDashBoard(String tag){
		registerLineToDashBoard(tag);
		registerRobotToDashBoard(tag+" robot");
	}
	@UtilFunctions
	Pose2d getCurrentPose();

	@UserRequirementFunctions
	void setColor(String color);
}
