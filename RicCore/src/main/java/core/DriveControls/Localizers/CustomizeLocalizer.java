package core.DriveControls.Localizers;

import androidx.annotation.NonNull;

import DriveControls.Localizers.LocalizerDefinition.Localizer;
import DriveControls.Localizers.LocalizerDefinition.LocalizerPlugin;
import DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import DriveControls.Localizers.LocalizerPlugins.DeadWheelLocalizer;
import DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import core.teamcode.Hardwares.Classic;
import core.teamcode.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class CustomizeLocalizer extends SubassemblyLocalizer implements Localizer {
	public CustomizeLocalizer(@NonNull Classic classic) {
		super(new LocalizerPlugin[]{
				new DeadWheelLocalizer(classic),
				new ImuLocalizer(classic)
		});
	}
}
