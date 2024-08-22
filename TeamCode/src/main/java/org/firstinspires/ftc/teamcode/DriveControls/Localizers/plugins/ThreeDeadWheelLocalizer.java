package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.DeadWheelEncoders;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.Annotation.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.utils.Complex;

@LocalizationPlugin
public class ThreeDeadWheelLocalizer implements PositionLocalizerPlugin {
	public DeadWheelEncoders encoders;
	public Pose2d RobotPosition;

	public ThreeDeadWheelLocalizer(@NonNull Classic classic){
		encoders=classic.encoders;
		RobotPosition=new Pose2d(0,0,0);
	}

	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}

	@Override
	public void update() {
		encoders.update();
		Complex delta=new Complex(
				Params.LateralInchPerTick*(encoders.LateralTicks-encoders.LastLateralTicks),
				Params.AxialInchPerTick*(encoders.AxialTicks-encoders.LastAxialTicks)
		);
		delta=delta.divide(new Complex(Params.TurningDegPerTick*(encoders.TurningTicks-encoders.LastTurningTicks)));
		RobotPosition=new Pose2d(
				RobotPosition.position.x+delta.RealPart,
				RobotPosition.position.y+delta.imaginary(),
				RobotPosition.heading.toDouble()+Math.toRadians(encoders.TurningTicks*Params.TurningDegPerTick)
		);
	}
}
