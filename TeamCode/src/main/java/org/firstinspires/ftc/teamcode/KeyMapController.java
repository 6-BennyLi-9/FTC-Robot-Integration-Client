package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

/**
 * @see Robot
 * @see KeyMap
 * */
public abstract class KeyMapController {
	@UserRequirementFunctions
	public abstract void KeyMapOverride(@NonNull KeyMap keyMap);
}
