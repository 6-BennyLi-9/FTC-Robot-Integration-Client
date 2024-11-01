package org.firstinspires.ftc.teamcode.keymap;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public abstract class KeyMapContent {
	public final boolean           IsControlledByGamePad1;
	public final KeyTag            tag;
	public final KeyMapSettingType setting;

	protected KeyMapContent(final KeyTag tag, final KeyMapSettingType setting) {
		this(tag, setting, true);
	}

	protected KeyMapContent(final KeyTag tag, final KeyMapSettingType setting, final boolean IsControlledByGamePad1) {
		this.tag = tag;
		this.setting = setting;
		this.IsControlledByGamePad1 = IsControlledByGamePad1;
	}

	@NonNull
	@Override
	public String toString() {
		return this.tag + "-" + this.setting + "-" + this.IsControlledByGamePad1;
	}
}
