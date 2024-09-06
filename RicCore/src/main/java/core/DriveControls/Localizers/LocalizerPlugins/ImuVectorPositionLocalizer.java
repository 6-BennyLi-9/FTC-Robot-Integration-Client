package core.DriveControls.Localizers.LocalizerPlugins;

import com.acmerobotics.roadrunner.Vector2d;

import DriveControls.Localizers.LocalizerDefinition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;

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
