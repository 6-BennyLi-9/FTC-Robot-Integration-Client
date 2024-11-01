package org.firstinspires.ftc.teamcode.drives.localizers.definition;

public interface HeadingLocalizerPlugin extends LocalizerPlugin{
	double getHeadingDeg();

	@Override
	default void drawRobot() {}
	@Override
	default void drawRobotWithoutSendingPacket(){}
}
