package org.firstinspires.ftc.teamcode.drives.localizers.definition;

import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

public interface Localizer {
	void update();
	default Position2d getCurrentPose(){return null;}
	void drawRobotWithoutSendingPacket();
	@ExtractedInterfaces
	default void drawRobot(){
		drawRobotWithoutSendingPacket();
		DashboardClient.getInstance().sendPacket();
	}
}
