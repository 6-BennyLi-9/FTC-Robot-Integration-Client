package org.firstinspires.ftc.teamcode.hardwares.integration.gamepads;

/**
 * 给予键或者操控杆的标签，用于查找与调用
 */
public enum KeyTag {
	Intake,
	Pop,
	ArmInIntake,
	ArmLowerPlacement,
	ArmHigherPlacement,
	ArmIDLE,
	SuspensionArm,

	ChassisRunForward, ChassisRunStrafe, ChassisTurn,
	/**
	 * 必须为 Rod 控制
	 */
	ChassisSpeedControl,
	/**
	 * 必须为 Button 控制
	 */
	ChassisSpeedConfig,

	TuningButton1,
	TuningButton2,
	TuningButton3,
	TuningButton4,
}
