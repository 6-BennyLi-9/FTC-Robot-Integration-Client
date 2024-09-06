package core.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import DriveControls.Localizers.LocalizerDefinition.Localizer;
import DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import DriveControls.Localizers.LocalizerPlugins.DeadWheelLocalizer;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Utils.Annotations.LocalizationSubassembly;

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
