package core.DriveControls.Localizers;

import androidx.annotation.NonNull;

import core.DriveControls.Localizers.LocalizerDefinition.Localizer;
import core.DriveControls.Localizers.LocalizerDefinition.LocalizerPlugin;
import core.DriveControls.Localizers.LocalizerDefinition.SubassemblyLocalizer;
import core.DriveControls.Localizers.LocalizerPlugins.DeadWheelLocalizer;
import core.DriveControls.Localizers.LocalizerPlugins.ImuLocalizer;
import core.Hardwares.Classic;
import core.Utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class CustomizeLocalizer extends SubassemblyLocalizer implements Localizer {
	public CustomizeLocalizer(@NonNull Classic classic) {
		super(new LocalizerPlugin[]{
				new DeadWheelLocalizer(classic),
				new ImuLocalizer(classic)
		});
	}
}
