package org.firstinspires.ftc.teamcode.Localizers.LocalizerDefinition;

import com.acmerobotics.roadrunner.Pose2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Pose2d getCurrentPose();
}
