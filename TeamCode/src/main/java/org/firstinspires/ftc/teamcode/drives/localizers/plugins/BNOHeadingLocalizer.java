package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.drives.localizers.definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Sensors;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationPlugin;

@LocalizationPlugin
public class BNOHeadingLocalizer implements HeadingLocalizerPlugin {
	private final Sensors sensors;
	public double RobotHeading;

	public BNOHeadingLocalizer(@NonNull final Chassis chassis){
		this.sensors = chassis.sensors;
	}

	@Override
	public double getHeadingDeg() {
		return this.RobotHeading;
	}

	@Override
	public void update() {
		this.sensors.imu.update();
		this.RobotHeading = this.sensors.robotAngle();
	}
}
