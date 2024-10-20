package org.firstinspires.ftc.teamcode.hardwares.integration.gamepads;

/**
 * 对键位的设置
 */
public enum KeyMapSettingType {
	/**
	 * 键按下后执行
	 */
	RunWhenButtonPressed,
	/**
	 * 键按下与松开时执行
	 */
	RunWhenButtonPressingBooleanChanged,
	/**
	 * 键按下时执行
	 */
	RunWhenButtonHold,
	/**
	 * 按下后改变执行 boolean
	 */
	SinglePressToChangeRunAble,
	/**
	 * 操纵杆
	 */
	PullRod
}
