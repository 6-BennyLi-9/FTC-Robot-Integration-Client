package core.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import core.DriveControls.Localizers.LocalizerDefinition.Localizer;
import core.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import core.DriveControls.Localizers.LocalizerPlugins.DeadWheelLocalizer;
import core.Hardwares.Classic;
import core.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class DeadWheelSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public DeadWheelSubassemblyLocalizer(Classic classic) {
		super(new DeadWheelLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}
