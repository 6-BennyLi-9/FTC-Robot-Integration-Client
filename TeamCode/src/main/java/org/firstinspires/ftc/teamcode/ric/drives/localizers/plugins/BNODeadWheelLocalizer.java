package org.firstinspires.ftc.teamcode.ric.drives.localizers.plugins;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.ric.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.ric.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;

/**
 * 使用imu获取机器的角度
 */
@LocalizationPlugin
public class BNODeadWheelLocalizer extends DeadWheelLocalizer implements PositionLocalizerPlugin {
	public BNODeadWheelLocalizer(Client client, Sensors sensors) {
		super(sensors);
	}

	@Override
	public void update() {
		super.update();
		sensors.imu.update();
		robotPosition=new Pose2d(robotPosition.position,Math.toRadians(sensors.imu.robotAngle));
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPosition;
	}
}
