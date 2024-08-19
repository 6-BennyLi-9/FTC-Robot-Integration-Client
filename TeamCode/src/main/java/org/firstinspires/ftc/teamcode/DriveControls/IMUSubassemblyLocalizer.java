package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.ImuLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;

public class IMUSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public Sensors sensors;
	public IMUSubassemblyLocalizer(Sensors sensors) {
		super(new ImuLocalizer(sensors));
	}

	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}
}
