package org.firstinspires.ftc.teamcode.keymap;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public class KeyMapButtonContent extends KeyMapContent {
	public final KeyButtonType type;

	public KeyMapButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting) {
		super(tag, setting);
		this.type = type;
	}

	public KeyMapButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting, boolean IsControlledByGamePad1) {
		super(tag, setting, IsControlledByGamePad1);
		this.type = type;
	}
}
