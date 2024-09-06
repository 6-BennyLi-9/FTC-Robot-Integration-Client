package core.DriveControls.Localizers.LocalizerPlugins;

import com.acmerobotics.roadrunner.Vector2d;

import DriveControls.Localizers.LocalizerDefinition.VectorPositionLocalizerPlugin;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public final class ImuVectorPositionLocalizer extends ImuLocalizer implements VectorPositionLocalizerPlugin {
	public ImuVectorPositionLocalizer(Classic classic){
		super(classic);
		this.sensors=classic.sensors;
	}

	@Override
	public Vector2d getCurrentVector() {
		return pose.position;
	}
}
