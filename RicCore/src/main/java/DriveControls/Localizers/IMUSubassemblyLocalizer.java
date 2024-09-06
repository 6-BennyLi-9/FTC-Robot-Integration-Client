package DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import DriveControls.Localizers.LocalizerDefinition.Localizer;
import DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationSubassembly;

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
