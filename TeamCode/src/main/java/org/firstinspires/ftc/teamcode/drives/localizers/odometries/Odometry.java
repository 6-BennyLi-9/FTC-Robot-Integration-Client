package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

public interface Odometry {
	/**
	 * @param relDeltaTheta 默认0°为X轴正方向，为角度制
	 */
	@UtilFunctions
	void update(double relDeltaX, double relDeltaY, double relDeltaTheta);
	@UtilFunctions
	default void registerLineToDashBoard(){}
	@UtilFunctions
	void registerRobotToDashBoard(String tag);
	@UtilFunctions
	default void registerToDashBoard(String tag){
		registerLineToDashBoard();
		registerRobotToDashBoard(tag+" robot");
	}
	@UtilFunctions
	default void drawToDashBoard(String tag){
		registerToDashBoard(tag);
		DashboardClient.getInstance().sendPacket();
	}
	@UtilFunctions
	Position2d getCurrentPose();

	@UserRequirementFunctions
	void setColor(String color);
}
