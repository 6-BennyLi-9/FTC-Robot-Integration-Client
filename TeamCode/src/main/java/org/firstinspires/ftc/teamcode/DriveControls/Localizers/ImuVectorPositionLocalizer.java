package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;

public final class ImuVectorPositionLocalizer extends ImuLocalizer implements VectorPositionLocalizerPlugin {
	public ImuVectorPositionLocalizer(Sensors sensors){
		super(sensors);
		this.sensors=sensors;
	}

	@Override
	public Vector2d getCurrentVector() {
		return pose.position;
	}
}
