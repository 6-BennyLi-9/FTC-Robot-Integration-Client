package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins.DeadWheelHeadingLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins.ImuVectorPositionLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationSubassembly;

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
