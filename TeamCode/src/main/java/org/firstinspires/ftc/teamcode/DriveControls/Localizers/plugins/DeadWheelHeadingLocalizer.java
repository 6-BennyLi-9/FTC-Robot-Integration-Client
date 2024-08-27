package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public final class DeadWheelHeadingLocalizer implements HeadingLocalizerPlugin {
	public Sensors sensors;
	public double HeadingDeg;

	public DeadWheelHeadingLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
	}

	@Override
	public double getHeadingDeg() {
		return HeadingDeg;
	}

	@Override
	public void update() {
		sensors.update();
		HeadingDeg=(sensors.LeftTick-sensors.RightTick)/2* Params.TurningDegPerTick;
	}
}
