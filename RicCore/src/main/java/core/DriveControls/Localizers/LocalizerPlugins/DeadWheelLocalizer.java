package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import DriveControls.Localizers.LocalizerDefinition.HeadingLocalizerPlugin;
import DriveControls.Localizers.LocalizerDefinition.PositionLocalizerPlugin;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	public DeadWheelVectorPositionLocalizer vectorLocalizer;
	public HeadingLocalizerPlugin headingLocalizer;
	public Pose2d RobotPosition;

	public DeadWheelLocalizer(@NonNull Classic classic){
		this(classic,new ImuHeadingLocalizer(classic));
	}
	public DeadWheelLocalizer(@NonNull Classic classic,@NonNull HeadingLocalizerPlugin plugin){
		vectorLocalizer=new DeadWheelVectorPositionLocalizer(classic);
		headingLocalizer=plugin;
	}
	@Override
	public Pose2d getCurrentPose() {
		return RobotPosition;
	}

	@Override
	public void update() {
		headingLocalizer.update();
		vectorLocalizer.SetHeadingRad(headingLocalizer.getHeadingDeg());
		vectorLocalizer.update();
		RobotPosition=new Pose2d(vectorLocalizer.getCurrentVector(),Math.toRadians(headingLocalizer.getHeadingDeg()));
	}
}
