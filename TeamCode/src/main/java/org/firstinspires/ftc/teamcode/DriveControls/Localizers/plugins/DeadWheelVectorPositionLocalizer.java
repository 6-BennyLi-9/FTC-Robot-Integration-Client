package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.VectorPositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.DeadWheelEncoders;

public class DeadWheelVectorPositionLocalizer implements VectorPositionLocalizerPlugin {
	public static class Params{
		/**
		 * 每Tick机器所平移的距离
		 */
		public static double LateralInchPerTick=0;
		/**
		 * 每Tick机器所前进的距离
		 */
		public static double AxialInchPerTick=0;
	}
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
				encoders.LateralTicks*Params.LateralInchPerTick,
				encoders.AxialTicks*Params.AxialInchPerTick
		);
	}
}
