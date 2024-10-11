package org.firstinspires.ftc.teamcode.ric;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;

/**
 * @see Robot
 * @see KeyMap
 * */
public abstract class KeyMapController {
	@UserRequirementFunctions
	public abstract void KeyMapOverride(@NonNull KeyMap keyMap);
}
