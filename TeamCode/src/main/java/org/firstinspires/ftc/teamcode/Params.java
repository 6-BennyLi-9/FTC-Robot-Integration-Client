package org.firstinspires.ftc.teamcode;

public final class Params {
	/**
	 * 每Tick机器所旋转的角度
	 */
	public static double TurningDegPerTick = 0;
	/**
	 * 每Tick机器所平移的距离
	 */
	public static double LateralInchPerTick=0;
	/**
	 * 每Tick机器所前进的距离
	 */
	public static double AxialInchPerTick=0;
	/**
	 * IMU相较于机器的正中心在X轴上的偏差
	 * @see org.firstinspires.ftc.teamcode.RIC_tuning.IMUPositionTuner
	 */
	public static double X_error=0;
	/**
	 * IMU相较于机器的正中心在Y轴上的偏差
	 * @see org.firstinspires.ftc.teamcode.RIC_tuning.IMUPositionTuner
	 */
	public static double Y_error=0;
	/**
	 * 用1f的力，在1s后所前行的距离，单位：inch (time(1s)*power(1f)) [sf/inch]
	 */
	public static double vP=0;
	/**
	 *positionErrorMargin，单位：inch
	 */
	public static double pem=0.5;
	/**
	 *angleErrorMargin，单位：度
	 */
	public static double aem=1;
	/**
	 * 机器的超时保护机制，如果超过该时间，机器仍未到达点位，则会强制取消点位的执行
	 */
	public static double timeOutProtectionMills=1000;
	/**
	 * 左侧死轮和右侧死轮的距离
	 */
	public static double LateralPosition=0;
	/**
	 * 机器中心到中间死轮(前端死轮)的距离
	 */
	public static double AxialPosition=0;
	/**
	 * 在执行手动程序时，由Classic下达的XPower命令的倍率因数
	 */
	public static double factorXPower=1;
	/**
	 * 在执行手动程序时，由Classic下达的YPower命令的倍率因数
	 */
	public static double factorYPower=1;
	/**
	 * 在执行手动程序时，由Classic下达的HeadingPower命令的倍率因数
	 */
	public static double factorHeadingPower=1;
	/**
	 * 在执行手动程序时，由Structure下达的IntakePower命令的倍率因数
	 */
	public static double factorIntakePower;
	/**
	 * 在执行手动程序时，由Structure下达的SuspensionArmPower命令的倍率因数
	 */
	public static double factorSuspensionArmPower;
}
