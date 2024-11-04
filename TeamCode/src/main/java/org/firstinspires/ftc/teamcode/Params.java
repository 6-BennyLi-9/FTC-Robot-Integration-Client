package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.codes.tunings.SecPowerPerInchTuner;
import org.firstinspires.ftc.teamcode.codes.tunings.ThreeInOne_DeadWheelTuner;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;
import org.firstinspires.ftc.teamcode.utils.clients.TelemetryClient;

@Config
public enum Params {
	;

	@Config
	public enum PIDParams{
		;
		//与底盘相关的kP理论值：SimpleMecanumDrive.Params.secPowerPerInch
		//TODO:预设...[0]为底盘X，[1]为底盘Y，[2]为底盘方向
		//TODO:若要更改，则请查看对该类型的访问中的序数是否需要改变
		public static final double[] kP= {0.12, 0.15, 0.12};
		public static final double[] kI= {0, 0, 0};
		public static final double[] kD= {0.04, 0.05, 0.04};
		public static final double[] MAX_I= {100, 100, 0};
	}
	/**
	 * 会在 Robot 的构造函数中修改
	 * @see Robot
	 * */
	@Config
	public enum Configs{
		;
		/**让机器自动在运行{@code update()}时，自动清除所有电机的{@code power}*/
		public static boolean autoPrepareForNextOptionWhenUpdate = true;
		/**让机器自动在执行提供的操作时，自动在更改任何{@code power}变量后执行{@code update()}*/
		public static boolean runUpdateWhenAnyNewOptionsAdded;
		/**在对舵机下达命令时，自动等待舵机到位*/
		public static boolean waitForServoUntilThePositionIsInPlace;
		/**让机器即使在不调用{@code pid}时，依然执行{@code pid.update()}*/
		public static boolean alwaysRunPIDInAutonomous;
		/**在自动程序中使用{@code pid}*/
		public static boolean usePIDToDriveInAutonomous                 = true;
		/**必须在自动程序中保持改变量为{@code true}*/
		public static boolean driverUsingAxisPowerInsteadOfCurrentPower =true;
		/**启用超时保护器*/
		public static boolean useOutTimeProtection = true;
		/**自动在初始化{@code IntegrationHardwareMap}时，登记所有硬件<p>适合单一队伍的程序*/
		public static boolean autoRegisterAllHardwaresWhenInit = true;

		/**
		 * 自动在{@code Client}定义时候加入 FtcDashboardTelemetry
		 * @see Client
		 */
		public static boolean clientAutoRegisteredFtcDashboardTelemetry=true;
		/**
		 * 自动在{@code TelemetryClient.addData}时更新 DashboardClient
		 * @see TelemetryClient#addData(String, Object)
		 * @see DashboardClient#put(Object, Object)
		 */
		public static boolean dashboardAutoSyncWithTelemetry=true;

		public static boolean sortDataInTelemetryClientUpdate=true;

		public static void reset(){
			driverUsingAxisPowerInsteadOfCurrentPower =true;
			runUpdateWhenAnyNewOptionsAdded =false;
			waitForServoUntilThePositionIsInPlace =false;
			autoPrepareForNextOptionWhenUpdate =false;
			alwaysRunPIDInAutonomous =false;
			usePIDToDriveInAutonomous =true;
			useOutTimeProtection =true;
			autoRegisterAllHardwaresWhenInit =true;
			clientAutoRegisteredFtcDashboardTelemetry =true;
			dashboardAutoSyncWithTelemetry =true;
			sortDataInTelemetryClientUpdate =true;
		}

		public static final String TuningAndTuneOpModesGroup = "0_Tunings";
		public static final String SampleOpModesGroup = "0_Samples";
	}
	@Config
	public enum HardwareNamespace {
		;
		public static final String LeftFront="leftFront";
		public static final String RightFront="rightFront";
		public static final String LeftRear="leftBack";
		public static final String RightRear="rightBack";
		public static final String PlacementArm="rightLift";
		public static final String Intake="intake";
		public static final String FrontClip="frontClip";
		public static final String RearClip="rearClip";
		public static final String SuspensionArm="rack";
		public static final String Imu="imu";
	}
	@Config
	public enum ServoConfigs{
		;
		public static double frontClipOpen;
		public static double rearClipOpen;
		public static double frontClipClose;
		public static double rearClipClose;
	}
	@Config
	public enum PositionalMotorConfigs{
		;
		public static int IDLEPlacement;
		public static int LowPlacement;
		public static int HighPlacement;
	}
	/**
	 * 每Tick机器所旋转的角度
	 * @see ThreeInOne_DeadWheelTuner
	 */
	public static final double TurningDegPerTick = 0.15;
	/**
	 * 每Tick机器所前进的距离
	 * @see ThreeInOne_DeadWheelTuner
	 */
	public static final double AxialInchPerTick=0.00114285714285714285714285714286;
	/**
	 * 每Tick机器所平移的距离
	 * @see ThreeInOne_DeadWheelTuner
	 */
	public static final double LateralInchPerTick= AxialInchPerTick;
	/**
	 * 用1f的力，在1s后所前行的距离，单位：inch (time(1s)*power(1f)) [sf/inch]
	 * @see SecPowerPerInchTuner
	 */
	public static final double secPowerPerInch = 24;
	/**
	 *positionErrorMargin，单位：inch
	 */
	public static final double pem=0.5;
	/**
	 *angleErrorMargin，单位：度
	 */
	public static final double aem=1;
	/**
	 * 机器的超时保护机制，如果超过该时间，机器仍未到达点位，则会强制取消点位的执行
	 */
	public static final double timeOutProtectionMills=1000;
	/**
	 * 左侧死轮和右侧死轮的距离，单位：inch
	 */
	public static final double LateralPosition=21;
	/**
	 * 机器中心到中间死轮(前端死轮)的距离，单位：inch
	 */
	public static final double AxialPosition=1;
	/**
	 * 在执行手动程序时，由Classic下达的XPower命令的倍率因数
	 */
	public static final double factorXPower=1;
	/**
	 * 在执行手动程序时，由Classic下达的YPower命令的倍率因数
	 */
	public static final double factorYPower=1;
	/**
	 * 在执行手动程序时，由Classic下达的HeadingPower命令的倍率因数
	 */
	public static final double factorHeadingPower=1;
	/**
	 * 在执行手动程序时，由Structure下达的IntakePower命令的倍率因数
	 */
	public static final double factorIntakePower=1;
	/**
	 * 在执行手动程序时，由Structure下达的SuspensionArmPower命令的倍率因数
	 */
	public static final double factorSuspensionArmPower=1;
}
