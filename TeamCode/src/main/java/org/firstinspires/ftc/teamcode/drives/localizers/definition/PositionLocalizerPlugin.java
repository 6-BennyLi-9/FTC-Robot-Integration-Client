package org.firstinspires.ftc.teamcode.drives.localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Pose2d getCurrentPose();
}
