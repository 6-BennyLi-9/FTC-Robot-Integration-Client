package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.Odometry;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	protected final Odometry odometry;
	protected final Sensors sensors;
	public Pose2d robotPosition;

	public DeadWheelLocalizer(Sensors sensors){
		odometry=new ArcOrganizedOdometer();
		this.sensors=sensors;
	}

	@Override
	public void update() {
		sensors.updateEncoders();//防止mspt过高
		odometry.update(sensors.getDeltaL()* Params.LateralInchPerTick ,sensors.getDeltaA() * Params.AxialInchPerTick,
				sensors.getDeltaT() * Params.TurningDegPerTick);
		robotPosition=odometry.getCurrentPose();
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPosition;
	}
}
