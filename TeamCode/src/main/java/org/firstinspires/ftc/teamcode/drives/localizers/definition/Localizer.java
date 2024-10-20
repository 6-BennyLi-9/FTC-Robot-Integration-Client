package org.firstinspires.ftc.teamcode.drives.localizers.definition;

import org.firstinspires.ftc.teamcode.utils.Position2d;

public interface Localizer {
	void update();
	default Position2d getCurrentPose(){return null;}
}
