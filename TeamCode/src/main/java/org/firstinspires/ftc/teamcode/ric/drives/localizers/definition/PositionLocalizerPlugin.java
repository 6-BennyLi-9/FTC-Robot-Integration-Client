package org.firstinspires.ftc.teamcode.ric.drives.localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Pose2d getCurrentPose();
}
