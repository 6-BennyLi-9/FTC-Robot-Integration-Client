package org.firstinspires.ftc.teamcode.drives.localizers.definition;

import org.firstinspires.ftc.teamcode.utils.Position2d;

public interface PositionLocalizerPlugin extends LocalizerPlugin{
	Position2d getCurrentPose();
}
