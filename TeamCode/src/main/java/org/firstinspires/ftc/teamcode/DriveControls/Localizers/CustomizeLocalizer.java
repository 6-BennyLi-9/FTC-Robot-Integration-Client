package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.LocalizerPlugin;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins.DeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class CustomizeLocalizer extends SubassemblyLocalizer implements Localizer {
	public CustomizeLocalizer(@NonNull Classic classic) {
		super(new LocalizerPlugin[]{
				new DeadWheelLocalizer(classic),
				new ImuLocalizer(classic)
		});
	}
}
