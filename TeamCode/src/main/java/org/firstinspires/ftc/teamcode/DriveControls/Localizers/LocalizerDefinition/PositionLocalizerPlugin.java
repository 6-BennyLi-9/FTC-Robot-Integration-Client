package org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition;

import com.acmerobotics.roadrunner.Pose2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Pose2d getCurrentPose();
}
