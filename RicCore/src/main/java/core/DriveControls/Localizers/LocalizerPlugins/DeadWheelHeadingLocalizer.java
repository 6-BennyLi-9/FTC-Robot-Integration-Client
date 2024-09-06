package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Hardwares.basic.Sensors;
import core.teamcode.Params;
import core.teamcode.Utils.Annotations.LocalizationPlugin;

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
