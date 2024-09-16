package org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public class BNOHeadingLocalizer implements HeadingLocalizerPlugin {
	private final Sensors sensors;
	public double RobotHeading;

	public BNOHeadingLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
	}

	@Override
	public double getHeadingDeg() {
		return RobotHeading;
	}

	@Override
	public void update() {
		sensors.imu.update();
		RobotHeading=sensors.RobotAngle();
	}
}
