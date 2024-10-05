package org.firstinspires.ftc.teamcode.localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

public interface Localizer {
	void update();
	default Pose2d getCurrentPose(){return null;}
}
