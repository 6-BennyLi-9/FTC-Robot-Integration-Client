package core.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import core.DriveControls.Localizers.LocalizerDefinition.Localizer;
import core.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import core.DriveControls.Localizers.LocalizerPlugins.DeadWheelHeadingLocalizer;
import core.DriveControls.Localizers.LocalizerPlugins.ImuVectorPositionLocalizer;
import core.Hardwares.Classic;
import core.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class SimpleSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public SimpleSubassemblyLocalizer(Classic classic) {
		super(new ImuVectorPositionLocalizer(classic),new DeadWheelHeadingLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}
