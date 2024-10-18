package org.firstinspires.ftc.teamcode.ric.keymap;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;

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

	@NonNull
	@Override
	public String toString() {
		return tag+"-"+setting+"-"+IsControlledByGamePad1;
	}
}
