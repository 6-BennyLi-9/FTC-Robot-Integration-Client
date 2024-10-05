package org.firstinspires.ftc.teamcode.localizers.plugins;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

/**
 * 使用imu获取机器的角度
 */
@LocalizationPlugin
public class BNODeadWheelLocalizer extends DeadWheelLocalizer implements PositionLocalizerPlugin {
	public BNODeadWheelLocalizer(Client client, Sensors sensors) {
		super(client, sensors);
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
