package org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public class ImuHeadingLocalizer implements HeadingLocalizerPlugin {
	public double HeadingDeg;
	public Sensors sensors;
	public ImuHeadingLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
	}
	@Override
	public double getHeadingDeg() {
		return HeadingDeg;
	}

	@Override
	public void update() {
		sensors.update();
		HeadingDeg=sensors.FirstAngle;
	}
}
