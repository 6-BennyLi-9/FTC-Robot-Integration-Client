package org.firstinpires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyButtonType;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyTag;
import org.firstinspires.ftc.teamcode.KeyMap;
import org.firstinspires.ftc.teamcode.KeyMapController;

/**
 * 可以将该类重命名为<code><队伍编号>KeyMap</code>
 */
public class TeamCodeKeyMap extends KeyMapController {
	@Override
	public void KeyMapOverride(@NonNull KeyMap keyMap) {
		//TODO:填入参数，格式如下
		keyMap.loadButtonContent(KeyTag.Intake, KeyButtonType.X, KeyMapSettingType.SinglePressToChangeRunAble);
	}
}
