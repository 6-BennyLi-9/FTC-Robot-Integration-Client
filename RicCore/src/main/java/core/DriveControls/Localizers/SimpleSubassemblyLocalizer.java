package core.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import DriveControls.Localizers.LocalizerDefinition.Localizer;
import DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import DriveControls.Localizers.LocalizerPlugins.DeadWheelHeadingLocalizer;
import DriveControls.Localizers.LocalizerPlugins.ImuVectorPositionLocalizer;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Utils.Annotations.LocalizationSubassembly;

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
