package org.firstinspires.ftc.teamcode.keymap;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public class KeyMapButtonContent extends KeyMapContent {
	public final KeyButtonType type;

	public KeyMapButtonContent(final KeyTag tag, final KeyButtonType type, final KeyMapSettingType setting) {
		super(tag, setting);
		this.type = type;
	}

	public KeyMapButtonContent(final KeyTag tag, final KeyButtonType type, final KeyMapSettingType setting, final boolean IsControlledByGamePad1) {
		super(tag, setting, IsControlledByGamePad1);
		this.type = type;
	}
}
