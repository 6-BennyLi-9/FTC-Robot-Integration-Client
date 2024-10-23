package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;

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
	Position2d getCurrentPose();

	@UserRequirementFunctions
	void setColor(String color);
}
