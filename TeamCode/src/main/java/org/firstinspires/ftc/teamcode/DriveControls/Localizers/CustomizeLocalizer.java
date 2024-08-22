package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.LocalizerPlugin;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.DeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.ImuLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Annotation.LocalizationSubassembly;

@LocalizationSubassembly
public class CustomizeLocalizer extends SubassemblyLocalizer implements Localizer {
	public CustomizeLocalizer(@NonNull Classic classic) {
		super(new LocalizerPlugin[]{
				new DeadWheelLocalizer(classic),
				new ImuLocalizer(classic)
		});
	}

	@Override
	public Pose2d getCurrentPose() {
		return null;
	}
}
