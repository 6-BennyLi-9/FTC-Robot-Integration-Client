package org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.DeadWheelEncoders;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.Annotation.LocalizationPlugin;

@LocalizationPlugin
public class DeadWheelHeadingLocalizer implements HeadingLocalizerPlugin {

	public DeadWheelEncoders encoders;
	public double HeadingDeg;

	public DeadWheelHeadingLocalizer(@NonNull Classic classic){
		encoders=classic.encoders;
	}

	@Override
	public double getHeadingDeg() {
		return HeadingDeg;
	}

	@Override
	public void update() {
		encoders.update();
		HeadingDeg=encoders.TurningTicks* Params.TurningDegPerTick;
	}
}
