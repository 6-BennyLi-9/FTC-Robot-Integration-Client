package core.DriveControls.Localizers.LocalizerDefinition;

import com.acmerobotics.roadrunner.Pose2d;

public interface Localizer {
	void update();
	default Pose2d getCurrentPose(){return null;}
}
