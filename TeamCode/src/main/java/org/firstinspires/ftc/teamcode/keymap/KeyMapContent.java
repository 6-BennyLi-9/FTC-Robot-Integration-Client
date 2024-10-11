package org.firstinspires.ftc.teamcode.keymap;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public abstract class KeyMapContent {
	public final boolean           IsControlledByGamePad1;
	public final KeyTag            tag;
	public final KeyMapSettingType setting;

	public KeyMapContent(KeyTag tag, KeyMapSettingType setting) {
		this(tag, setting, true);
	}

	public KeyMapContent(KeyTag tag, KeyMapSettingType setting, boolean IsControlledByGamePad1) {
		this.tag = tag;
		this.setting = setting;
		this.IsControlledByGamePad1 = IsControlledByGamePad1;
	}
}
