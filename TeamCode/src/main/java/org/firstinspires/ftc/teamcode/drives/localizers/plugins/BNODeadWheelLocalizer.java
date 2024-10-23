package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

/**
 * 使用imu获取机器的角度
 */
@LocalizationPlugin
public class BNODeadWheelLocalizer extends DeadWheelLocalizer implements PositionLocalizerPlugin {
	public BNODeadWheelLocalizer(Sensors sensors) {
		super(sensors);
	}

	@Override
	public void update() {
		super.update();
		sensors.imu.update();
		robotPosition=new Position2d(robotPosition.asVector(),Math.toRadians(sensors.imu.robotAngle));
	}

	@Override
	public Position2d getCurrentPose() {
		return robotPosition;
	}
}
