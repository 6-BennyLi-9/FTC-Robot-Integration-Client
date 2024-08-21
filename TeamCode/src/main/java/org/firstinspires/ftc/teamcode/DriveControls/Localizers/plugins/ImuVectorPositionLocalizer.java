package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;

public final class ImuVectorPositionLocalizer extends ImuLocalizer implements VectorPositionLocalizerPlugin {
	public ImuVectorPositionLocalizer(Classic classic){
		super(classic);
		this.sensors=classic.sensors;
	}

	@Override
	public Vector2d getCurrentVector() {
		return pose.position;
	}
}
