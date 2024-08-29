package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
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
