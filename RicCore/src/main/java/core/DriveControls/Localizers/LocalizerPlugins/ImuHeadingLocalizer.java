package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import core.DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import core.Hardwares.Classic;
import core.Hardwares.basic.Sensors;
import core.Utils.Annotations.LocalizationPlugin;

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
