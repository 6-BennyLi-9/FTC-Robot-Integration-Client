package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Annotation.LocalizationPlugin;

@LocalizationPlugin
public class ThreeDeadWheelVectorPositionLocalizer extends ThreeDeadWheelLocalizer implements VectorPositionLocalizerPlugin {
	public ThreeDeadWheelVectorPositionLocalizer(@NonNull Classic classic){
		super(classic);
	}

	@Override
	public Vector2d getCurrentVector() {
		return RobotPosition.position;
	}
}
