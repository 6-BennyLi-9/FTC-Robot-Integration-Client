package core.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import core.DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import core.DriveControls.Localizers.LocalizerDefinition.Localizer;
import core.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import core.Hardwares.Classic;
import core.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class IMUSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public IMUSubassemblyLocalizer(Classic classic) {
		super(new ImuLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}
