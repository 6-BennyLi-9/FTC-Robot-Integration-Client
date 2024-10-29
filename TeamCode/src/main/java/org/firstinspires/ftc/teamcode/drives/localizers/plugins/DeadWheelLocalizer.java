package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.Odometry;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	protected final Odometry odometry;
	protected final Sensors sensors;
	public Position2d robotPosition;

	public DeadWheelLocalizer(Sensors sensors){
		odometry=new ArcOrganizedOdometer();
		this.sensors=sensors;
	}

	@Override
	public void update() {
		sensors.updateEncoders();//防止 mspt 过高
		odometry.update(sensors.getDeltaLateralInch(),sensors.getDeltaAxialInch(),sensors.getDeltaTurningDeg());
		robotPosition=odometry.getCurrentPose();
	}

	@Override
	public Position2d getCurrentPose() {
		return robotPosition;
	}
}
