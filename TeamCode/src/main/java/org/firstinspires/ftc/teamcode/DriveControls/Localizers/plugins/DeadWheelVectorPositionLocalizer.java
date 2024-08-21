package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import static org.firstinspires.ftc.teamcode.Params.AxialInchPerTick;
import static org.firstinspires.ftc.teamcode.Params.LateralInchPerTick;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.DeadWheelEncoders;

public class DeadWheelVectorPositionLocalizer implements VectorPositionLocalizerPlugin {
	public Vector2d RobotPosition;
	public DeadWheelEncoders encoders;

	public DeadWheelVectorPositionLocalizer(@NonNull Classic classic){
		encoders=classic.encoders;
	}

	@Override
	public Vector2d getCurrentVector() {
		return null;
	}

	@Override
	public void update() {
		encoders.update();
		RobotPosition=new Vector2d(
				encoders.LateralTicks* LateralInchPerTick,
				encoders.AxialTicks* AxialInchPerTick
		);
	}
}
