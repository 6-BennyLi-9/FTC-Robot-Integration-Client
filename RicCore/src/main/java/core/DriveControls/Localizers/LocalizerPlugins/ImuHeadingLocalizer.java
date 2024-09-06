package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Hardwares.basic.Sensors;
import core.teamcode.Utils.Annotations.LocalizationPlugin;

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
