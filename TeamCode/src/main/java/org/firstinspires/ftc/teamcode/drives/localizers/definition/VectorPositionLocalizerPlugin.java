package org.firstinspires.ftc.teamcode.drives.localizers.definition;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.utils.Position2d;

public interface VectorPositionLocalizerPlugin extends LocalizerPlugin{
	Vector2d getCurrentVector();
	default Position2d getCurrentPose(){
		return null;
	}
	default void SetHeadingRad(double Rad){}
}
