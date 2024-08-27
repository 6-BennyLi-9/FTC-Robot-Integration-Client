package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Annotations.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.utils.Complex;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	public Sensors sensors;
	public Pose2d RobotPosition;

	public DeadWheelLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
		RobotPosition=new Pose2d(0,0,0);
	}
	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}

	@Override
	public void update() {
		sensors.update();
		Complex delta;
		switch (sensors.DeadWheelType) {
			case BE_NOT_USING_DEAD_WHEELS:
				break;
			case TwoDeadWheels:

			case ThreeDeadWheels:

				break;
		}
	}
}
