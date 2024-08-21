package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.DeadWheelHeadingLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.DeadWheelVectorPositionLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;

public class DeadWheelSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public DeadWheelSubassemblyLocalizer(Classic classic) {
		super(new DeadWheelVectorPositionLocalizer(classic), new DeadWheelHeadingLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}