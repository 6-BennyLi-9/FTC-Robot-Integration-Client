package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 用于多个队伍进行键位的切换
 * @see Robot
 * @see KeyMap
 * */
public abstract class KeyMapController {
	@UserRequirementFunctions
	public abstract void KeyMapOverride(@NonNull KeyMap keyMap);
}
