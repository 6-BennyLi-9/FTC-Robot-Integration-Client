package org.firstinspires.ftc.teamcode.Localizers.Plugin;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Chassis;
import org.firstinspires.ftc.teamcode.Localizers.Definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;

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
