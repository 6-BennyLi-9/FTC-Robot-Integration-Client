package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.LocalizerPlugin;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.DeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.ImuLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class CustomizeLocalizer extends SubassemblyLocalizer implements Localizer {
	public CustomizeLocalizer(@NonNull Classic classic) {
		super(new LocalizerPlugin[]{
				new DeadWheelLocalizer(classic),
				new ImuLocalizer(classic)
		});
	}
}
