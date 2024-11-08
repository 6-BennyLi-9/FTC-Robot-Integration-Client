package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.utils.enums.RunningMode.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.actions.Actions;
import org.firstinspires.ftc.teamcode.actions.utils.SleepingAction;
import org.firstinspires.ftc.teamcode.drives.controls.MecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.Structure;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;
import org.firstinspires.ftc.teamcode.hardwares.controllers.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Motors;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Sensors;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Servos;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.utils.ActionBox;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.enums.RobotState;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.utils.exceptions.UnKnownErrorsException;

/**
 * 几乎所有关于机器的集成化控制器
 */
public class Robot {
	public IntegrationHardwareMap lazyIntegratedDevices;

	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public final Chassis chassis;
	public final Structure structure;
	public final Webcam webcam;

	public final Client client;
	public final PidProcessor pidProcessor;

	public static RobotState robotState=RobotState.IDLE;
	public static RunningMode runningState;
	public IntegrationGamepad gamepad;
	public final ActionBox actionBox;
	public DriverProgram drive;

	public final Timer timer;

	public ParamsController paramsController =new DefaultParamsController();
	public KeyMapController keyMapController =new DefaultKeyMapController();


	public Robot(@NonNull final HardwareMap hardwareMap, @NonNull final RunningMode state, @NonNull final Telemetry telemetry){
		this(hardwareMap,state,new Client(telemetry));
	}

	public Robot(@NonNull final HardwareMap hardwareMap, @NonNull final RunningMode state, @NonNull final Client client){
		Params.Configs.reset();
		Global.clear();

		this.pidProcessor =new PidProcessor();

		this.lazyIntegratedDevices =new IntegrationHardwareMap(hardwareMap, this.pidProcessor);

		this.motors =new Motors(this.lazyIntegratedDevices);
		this.sensors =new Sensors(this.lazyIntegratedDevices);
		this.servos =new Servos(this.lazyIntegratedDevices);

		this.chassis =new Chassis(this.motors, this.sensors);
		this.structure =new Structure(this.motors, this.servos);
		this.webcam =new Webcam(hardwareMap);

		this.client=client;


		//TODO:如果需要，在这里修改 Params.Config 中的值
		switch (state) {
			case Autonomous:
				Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=true;

				this.InitInAutonomous();
				break;
			case ManualDrive:
				Params.Configs.runUpdateWhenAnyNewOptionsAdded=true;
				Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=false;

				this.InitInManualDrive();
				break;
			case Debug:case Sample:case TestOrTune:
				break;
			default:
				throw new UnKnownErrorsException("Unexpected runningState value:"+state.name());
		}

		runningState = state;
		this.actionBox = new ActionBox();
		this.timer =new Timer();
		client.addData("RobotState","UnKnow");

		Global.setRobot(this);
	}

	@UserRequirementFunctions
	public void setParamsOverride(final ParamsController controller){
		paramsController =controller;
		paramsController.PramsOverride();
	}
	@UserRequirementFunctions
	public void setKeyMapController(final KeyMapController controller){
		this.keyMapController =controller;
		this.keyMapController.KeyMapOverride(this.gamepad.keyMap);
	}

	/**
	 * 自动初始化SimpleMecanumDrive
	 * @return 返回定义好的SimpleMecanumDrive
	 */
	public DriverProgram InitMecanumDrive(final Position2d RobotPosition){
		this.drive =new SimpleMecanumDrive(RobotPosition);
		if(Autonomous != runningState) {
			Log.w("Robot.java","Initialized Driving Program in Manual Driving RobotState.");
		}
		return this.drive;
	}

	private void InitInAutonomous(){
		this.structure.clipOption(ClipPosition.Close);
		robotState = RobotState.IDLE;
		this.SetGlobalBufPower(0.9f);
	}
	private void InitInManualDrive(){
		this.structure.clipOption(ClipPosition.Open);
		robotState = RobotState.ManualDriving;
		this.SetGlobalBufPower(0.9f);
	}

	/**
	 * 自动转译成集成化的gamepad
	 * @param gamepad1 于 OpMode 中的 gamepad1
	 * @param gamepad2 于 OpMode 中的 gamepad2
	 *
	 * @see OpMode#gamepad1
	 * @see OpMode#gamepad2
	 */
	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		this.gamepad =new IntegrationGamepad(gamepad1,gamepad2);

		Global.currentGamepad1=gamepad1;
		Global.currentGamepad2=gamepad2;
		Global.integrationGamepad= this.gamepad;
	}

	/**
	 * 更新传感器、舵机、电机
	 *
	 * @see Sensors#update()
	 * @see Servos#update()
	 * @see Motors#update()
	 */
	public void updateHardwares(){
		this.sensors.update();
		this.servos.update();

		try{
			if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower) {
				this.motors.update(this.sensors.robotAngle());
			}else{
				this.motors.update();
			}
		}catch (final DeviceDisabledException ignored){}
	}
	/**
	 * 更新传感器、舵机、电机
	 * <p>
	 * 运行 ActionBox 内的所有 Action
	 *
	 * @see #updateHardwares()
	 * @see Sensors#update()
	 * @see Servos#update()
	 * @see Motors#update()
	 * @see ActionBox#output()
	 */
	public void update()  {
		if(90000 <= timer.stopAndGetDeltaTime() && ManualDrive == runningState){
			robotState = RobotState.FinalState;
		}

		this.updateHardwares();

		Actions.runAction(this.actionBox.output());
		this.client.changeData("RobotState", robotState.name());

		while(Params.Configs.waitForServoUntilThePositionIsInPlace && this.servos.inPlace()){
			this.servos.update();
			//当前最方便的Sleep方案
			Actions.runAction(new SleepingAction(100));
		}
	}

	/**
	 * 不会自动 #update()
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void operateThroughGamePad() {
		this.chassis.operateThroughGamePad(this.gamepad);
		this.structure.operateThroughGamePad(this.gamepad);
	}

	public DriveOrderBuilder DrivingOrderBuilder(){
		if(this.drive instanceof SimpleMecanumDrive)
			return ((SimpleMecanumDrive) this.drive).drivingCommandsBuilder();
		else if(this.drive instanceof MecanumDrive)
			return ((MecanumDrive) this.drive).drivingCommandsBuilder();
		return null;
	}

	/**
	 * 在该节点让机器旋转指定角度
	 * @param angle 要转的角度[-180,180)
	 */
	public void turnAngle(final double angle){
		if(ManualDrive == runningState)return;
		this.drive.runOrderPackage(this.DrivingOrderBuilder().TurnAngle(angle).END());
	}
	public void strafeTo(final Vector2d pose){
		if(ManualDrive == runningState)return;
		this.drive.runOrderPackage(this.DrivingOrderBuilder().StrafeTo(pose).END());
	}

	/**
	 * 会自动update()
	 */
	public Position2d pose(){
		this.drive.update();
		return this.drive.getCurrentPose();
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(final double BufPower){
		this.motors.setBufPower(BufPower);
	}

	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(final String key, final String val){
		this.client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(final String key, final Object val){
		this.client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteData(final String key){try{
		this.client.deleteData(key);}catch (final Exception ignored){}}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(final String key, final String val){
		this.client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(final String key, final Object val){
		this.client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(final String val){
		this.client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(final Object val){
		this.client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeLine(@NonNull final Object key, @NonNull final Object val){
		this.client.changeLine(key.toString(),val.toString());}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteLine(final String key){try{
		this.client.deleteLine(key);}catch (final Exception ignored){}}
}
