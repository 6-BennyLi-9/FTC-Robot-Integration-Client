package org.firstinspires.ftc.teamcode.keymap;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public class KeyMapRodContent extends KeyMapContent {
	public final KeyRodType type;

	public KeyMapRodContent(KeyTag tag, KeyRodType type) {
		super(tag, KeyMapSettingType.PullRod);
		this.type = type;
	}

	public KeyMapRodContent(KeyTag tag, KeyRodType type, boolean IsControlledByGamePad1) {
		super(tag, KeyMapSettingType.PullRod, IsControlledByGamePad1);
		this.type = type;
	}
}
