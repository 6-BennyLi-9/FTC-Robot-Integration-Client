package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.HeadingLocalizerPlugin;

public class TwoDeadWheelHeadingLocalizer implements HeadingLocalizerPlugin {
	double HeadingDeg;

	@Override
	public double getHeadingDeg() {
		return 0;
	}

	@Override
	public void update() {

	}
}
