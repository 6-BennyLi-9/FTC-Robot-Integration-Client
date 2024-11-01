package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.Odometry;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Sensors;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	protected final Odometry odometry;
	protected final Sensors sensors;
	public Position2d robotPosition;

	public DeadWheelLocalizer(final Sensors sensors){
		this.odometry =new ArcOrganizedOdometer();
		this.sensors=sensors;
	}

	@Override
	public void update() {
		this.sensors.updateEncoders();//防止 mspt 过高
		this.odometry.update(this.sensors.getDeltaLateralInch(), this.sensors.getDeltaAxialInch(), this.sensors.getDeltaTurningDeg());
		this.robotPosition = this.odometry.getCurrentPose();
	}

	@Override
	public Position2d getCurrentPose() {
		return this.robotPosition;
	}

	@Override
	public void drawRobotWithoutSendingPacket() {
		this.odometry.registerToDashBoard(getClass().getSimpleName());
	}
}
