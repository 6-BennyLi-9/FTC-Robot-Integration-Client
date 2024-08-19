package org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Pose2d getCurrentPose();
}
