package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import core.DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import core.Hardwares.Classic;
import core.Hardwares.basic.Sensors;
import core.Params;
import core.Utils.Annotations.LocalizationPlugin;

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
