package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import core.DriveControls.Localizers.LocalizerDefinition.PositionLocalizerPlugin;
import core.Hardwares.Classic;
import core.Hardwares.basic.Sensors;
import core.Params;
import core.Utils.Annotations.LocalizationPlugin;
import core.Utils.Complex;

@LocalizationPlugin
public class NewImuLocalizer implements PositionLocalizerPlugin {
	public Sensors sensors;
	public Complex error;
	public Pose2d RobotPosition;

	public NewImuLocalizer(@NonNull Classic classic){
		this.sensors=classic.sensors;
		error=new Complex(Params.X_error, Params.Y_error);
		RobotPosition=new Pose2d(0,0,0);
	}
	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}

	@Override
	public void update() {
		sensors.update();
		Complex delta=new Complex(sensors.XMoved-sensors.LastXMoved,sensors.YMoved-sensors.LastYMoved);
		delta=delta.divide(new Complex(sensors.FirstAngle-sensors.LastFirstAngle));
		RobotPosition=new Pose2d(
				RobotPosition.position.x+delta.RealPart,
				RobotPosition.position.y+delta.imaginary(),
				sensors.FirstAngle
		);
	}
}
