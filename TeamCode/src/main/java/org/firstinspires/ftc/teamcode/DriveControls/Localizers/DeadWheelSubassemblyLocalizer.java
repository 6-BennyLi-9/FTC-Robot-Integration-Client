package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.DeadWheelHeadingLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.ThreeDeadWheelVectorPositionLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Annotation.LocalizationSubassembly;

@LocalizationSubassembly
public class DeadWheelSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public DeadWheelSubassemblyLocalizer(Classic classic) {
		super(new ThreeDeadWheelVectorPositionLocalizer(classic), new DeadWheelHeadingLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}
