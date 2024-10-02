package org.firstinspires.ftc.teamcode.Localizers.Plugin;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Localizers.Definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

/**
 * 使用imu获取机器的角度
 */
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
