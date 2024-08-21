package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.ImuLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;

public class IMUSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public IMUSubassemblyLocalizer(Classic classic) {
		super(new ImuLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}
}
