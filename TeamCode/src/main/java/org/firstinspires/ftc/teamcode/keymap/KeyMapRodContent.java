package org.firstinspires.ftc.teamcode.keymap;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public class KeyMapRodContent extends KeyMapContent {
	public final KeyRodType type;

	public KeyMapRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting) {
		super(tag, setting);
		this.type = type;
	}

	public KeyMapRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting, boolean IsControlledByGamePad1) {
		super(tag, setting, IsControlledByGamePad1);
		this.type = type;
	}
}
