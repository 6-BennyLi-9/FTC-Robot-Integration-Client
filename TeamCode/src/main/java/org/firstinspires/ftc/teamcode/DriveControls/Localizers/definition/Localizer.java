package org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

public interface Localizer {
	void update();
	Pose2d getCurrentPose();
}
