package org.firstinspires.ftc.teamcode.Localizers.Definition;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public interface VectorPositionLocalizerPlugin extends LocalizerPlugin{
	Vector2d getCurrentVector();
	default Pose2d getCurrentPose(){
		return null;
	}
	default void SetHeadingRad(double Rad){}
}
