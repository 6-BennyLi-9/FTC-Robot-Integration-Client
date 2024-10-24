package org.firstinspires.ftc.teamcode.hardwares.integration.gamepads;

import org.firstinspires.ftc.teamcode.utils.annotations.Beta;

/**
 * 对键位的设置
 */
public enum KeyMapSettingType {
	/**
	 * 键按下后执行
	 */
	@Beta
	RunWhenButtonPressed,
	/**
	 * 键按下与松开时执行
	 */
	@Beta
	RunWhenButtonPressingBooleanChanged,
	/**
	 * 键按下时执行
	 */
	RunWhenButtonHold,
	/**
	 * 按下后改变执行 boolean
	 */
	@Beta
	SinglePressToChangeRunAble,
	/**
	 * 操纵杆
	 */
	PullRod
}
