package org.firstinspires.ftc.teamcode;

/**
 * 要修改其中的值请勿直接在这里修改，而是在<code>Robot</code>中修改
 */
public final class RuntimeOption {
	/**让机器自动在运行<code>update()</code>时，自动清除所有电机的<code>power</code>*/
	public static boolean autoPrepareForNextOptionWhenUpdate = true;
	/**让机器自动在执行提供的操作时，自动在更改任何<code>power</code>变量后执行<code>update()</code>*/
	public static boolean runUpdateWhenAnyNewOptionsAdded = false;
	/**在对舵机下达命令时，自动等待舵机到位*/
	public static boolean waitForServoUntilThePositionIsInPlace = false;
	/**让机器即使在不调用<code>pid</code>时，依然执行<code>pid.update()</code>*/
	public static boolean alwaysRunPIDInAutonomous = false;
	/**在自动程序中使用<code>pid</code>*/
	public static boolean usePIDInAutonomous = true;
	/**必须在自动程序中保持改变量为<code>true</code>*/
	public static boolean driverUsingAxisPowerInsteadOfCurrentPower=true;
	/**在手动程序中，使用<code>gamepad1</code>的<code>right_stick_y</code>来控制底盘速度*/
	public static boolean useRightStickYToConfigRobotSpeed = true;
	/**启用超时保护器*/
	public static boolean useOutTimeProtection = true;
}
