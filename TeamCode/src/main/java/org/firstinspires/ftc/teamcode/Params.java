package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Params {
	@Config
	public static class PIDParams{
		//与底盘相关的kP理论值：SimpleMecanumDrive.Params.secPowerPerInch
		//TODO:预设...[0]为底盘X，[1]为底盘Y，[2]为底盘方向
		//TODO:若要更改，则请查看对该类型的访问中的序数是否需要改变
		public static double[] kP= new double[]{0.12, 0.15, 0.12};
		public static double[] kI= new double[]{0, 0, 0};
		public static double[] kD= new double[]{0.04, 0.05, 0.04};
		public static double[] MAX_I= new double[]{100, 100, 0};
	}
	/**
	 * 会在 Robot 的构造函数中修改
	 * @see Robot
	 * */
	@Config
	public static class Configs{
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
		/**自动在初始化<code>IntegrationHardwareMap</code>时，登记所有硬件<p>适合单一队伍的程序*/
		public static boolean autoRegisterAllHardwaresWhenInit = false;
	}
	@Config
	public static class namespace{
		public static String LeftFront="leftFront";
		public static String RightFront="rightFront";
		public static String LeftRear="leftBack";
		public static String RightRear="rightRear";
		public static String PlacementArm="rightLift";
		public static String Intake="intake";
		public static String FrontClip="frontClip";
		public static String RearClip="rearClip";
		public static String SuspensionArm="rack";
		public static String Imu="imu";
	}
	@Config
	public static class ServoConfigs{
		public static double frontClipOpen=0;
		public static double rearClipOpen=0;
		public static double frontClipClose=0;
		public static double rearClipClose=0;
	}
	/**
	 * 每Tick机器所旋转的角度
	 * @see org.firstinspires.ftc.teamcode.Tuning.ThreeInOne_DeadWheelTuner
	 */
	public static double TurningDegPerTick = 0.01339983622422392615201369761;
	/**
	 * 每Tick机器所前进的距离
	 * @see org.firstinspires.ftc.teamcode.Tuning.ThreeInOne_DeadWheelTuner
	 */
	public static double AxialInchPerTick=0.001131541725601131541725601132;
	/**
	 * 每Tick机器所平移的距离
	 * @see org.firstinspires.ftc.teamcode.Tuning.ThreeInOne_DeadWheelTuner
	 */
	public static double LateralInchPerTick=AxialInchPerTick;
	/**
	 * IMU相较于机器的正中心在X轴上的偏差
	 * @see org.firstinspires.ftc.teamcode.Tuning.IMUPositionTuner
	 */
	@Deprecated
	public static double X_error=0;
	/**
	 * IMU相较于机器的正中心在Y轴上的偏差
	 * @see org.firstinspires.ftc.teamcode.Tuning.IMUPositionTuner
	 */
	@Deprecated
	public static double Y_error=0;
	/**
	 * 用1f的力，在1s后所前行的距离，单位：inch (time(1s)*power(1f)) [sf/inch]
	 */
	public static double secPowerPerInch =0;
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
	public static double LateralPosition=21;
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
	public static double factorIntakePower=1;
	/**
	 * 在执行手动程序时，由Structure下达的SuspensionArmPower命令的倍率因数
	 */
	public static double factorSuspensionArmPower=1;
	/**
	 * 电机的转力从0到其余数的保护时间，单位：ms
	 */
	public static double switchFromStaticToKinetic =75;
}
