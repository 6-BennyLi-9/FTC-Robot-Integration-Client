package org.firstinspires.ftc.teamcode.localizers.plugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.localizers.definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

@LocalizationPlugin
public class BNOHeadingLocalizer implements HeadingLocalizerPlugin {
	private final Sensors sensors;
	public double RobotHeading;

	public BNOHeadingLocalizer(@NonNull Chassis chassis){
		sensors= chassis.sensors;
	}

	@Override
	public double getHeadingDeg() {
		return RobotHeading;
	}

	@Override
	public void update() {
		sensors.imu.update();
		RobotHeading=sensors.robotAngle();
	}
}
