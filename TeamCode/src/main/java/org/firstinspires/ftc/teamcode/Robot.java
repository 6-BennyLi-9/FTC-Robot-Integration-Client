package org.firstinspires.ftc.teamcode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drives.controls.MecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.Structure;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;
import org.firstinspires.ftc.teamcode.hardwares.basic.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationHardwareMap;
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

public class Robot {
	public IntegrationHardwareMap lazyIntegratedDevices;

	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public Chassis chassis;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PidProcessor pidProcessor;

	public static RobotState robotState=RobotState.IDLE;
	public static RunningMode runningState;
	public IntegrationGamepad gamepad=null;
	public final ActionBox actionBox;
	public DriverProgram drive=null;

	public Timer timer;

	public ParamsController paramsController =new DefaultParamsController();
	public KeyMapController keyMapController =new DefaultKeyMapController();


	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningMode state, @NonNull Telemetry telemetry){
		this(hardwareMap,state,new Client(telemetry));
	}

	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningMode state, @NonNull Client client){
		Params.Configs.reset();

		pidProcessor=new PidProcessor();

		lazyIntegratedDevices=new IntegrationHardwareMap(hardwareMap,pidProcessor);

		motors=new Motors(lazyIntegratedDevices);
		sensors=new Sensors(lazyIntegratedDevices);
		servos=new Servos(lazyIntegratedDevices);

		chassis =new Chassis(motors,sensors);
		structure=new Structure(motors,servos);
		webcam=new Webcam(hardwareMap);

		this.client=client;
		pidProcessor=new PidProcessor();


		//TODO:如果需要，在这里修改 Params.Config 中的值
		switch (state) {
			case Autonomous:
				Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=true;

				InitInAutonomous();
				break;
			case ManualDrive:
				Params.Configs.runUpdateWhenAnyNewOptionsAdded=true;
				Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=false;

				InitInManualDrive();
				break;
			case Debug:case Sample:case TestOrTune:
				break;
			default:
				throw new UnKnownErrorsException("Unexpected runningState value:"+state.name());
		}

		runningState = state;
		actionBox = new ActionBox();
		timer=new Timer();
		client.addData("RobotState","UnKnow");

		Global.setRobot(this);
	}

	@UserRequirementFunctions
	public void setParamsOverride(ParamsController controller){
		this.paramsController =controller;
		this.paramsController.PramsOverride();
	}
	@UserRequirementFunctions
	public void setKeyMapController(KeyMapController controller){
		keyMapController=controller;
		keyMapController.KeyMapOverride(gamepad.keyMap);
	}

	/**
	 * 自动初始化SimpleMecanumDrive
	 * @return 返回定义好的SimpleMecanumDrive
	 */
	public DriverProgram InitMecanumDrive(Position2d RobotPosition){
		drive=new SimpleMecanumDrive(RobotPosition);
		if(runningState != RunningMode.Autonomous) {
			Log.w("Robot.java","Initialized Driving Program in Manual Driving RobotState.");
		}
		return drive;
	}

	private void InitInAutonomous(){
		structure.clipOption(ClipPosition.Close);
		robotState = RobotState.IDLE;
		SetGlobalBufPower(0.9f);
	}
	private void InitInManualDrive(){
		structure.clipOption(ClipPosition.Open);
		robotState = RobotState.ManualDriving;
		SetGlobalBufPower(0.9f);
	}

	public void registerGamepad(Gamepad gamepad1,Gamepad gamepad2){
		gamepad=new IntegrationGamepad(gamepad1,gamepad2);

		Global.currentGamepad1=gamepad1;
		Global.currentGamepad2=gamepad2;
		Global.integrationGamepad=gamepad;
	}

	public void update()  {
		if(timer.stopAndGetDeltaTime()>=90000&&runningState==RunningMode.ManualDrive){
			robotState = RobotState.FinalState;
		}

		sensors.update();
		servos.update();

		try{
			if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower) {
				motors.update(sensors.robotAngle());
			}else{
				motors.update();
			}
		}catch (DeviceDisabledException ignored){}

		Actions.runBlocking(actionBox.output());
		client.changeData("RobotState", robotState.name());

		while(Params.Configs.waitForServoUntilThePositionIsInPlace && servos.inPlace()){
			servos.update();
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}

	/**
	 * 不会自动 update()
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void operateThroughGamePad() {
		chassis.operateThroughGamePad(gamepad);
		structure.operateThroughGamePad(gamepad);
	}

	public DriveOrderBuilder DrivingOrderBuilder(){
		if(drive instanceof SimpleMecanumDrive)
			return ((SimpleMecanumDrive) drive).drivingCommandsBuilder();
		else if(drive instanceof MecanumDrive)
			return ((MecanumDrive) drive).drivingCommandsBuilder();
		return null;
	}

	/**
	 * 在该节点让机器旋转指定角度
	 * @param angle 要转的角度[-180,180)
	 */
	public void turnAngle(double angle){
		if(runningState == RunningMode.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().TurnAngle(angle).END());
	}
	public void strafeTo(Vector2d pose){
		if(runningState == RunningMode.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().StrafeTo(pose).END());
	}

	/**
	 * 会自动update()
	 */
	public Position2d pose(){
		drive.update();
		return drive.getCurrentPose();
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(double BufPower){
		motors.setBufPower(BufPower);
	}

	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(String key, String val){client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(String key,Object val){client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteData(String key){try{client.deleteData(key);}catch (Exception ignored){}}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(String key, String val){client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(String key,Object val){client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(String val){client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(Object val){client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeLine(@NonNull Object key, @NonNull Object val){client.changeLine(key.toString(),val.toString());}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteLine(String key){try{client.deleteLine(key);}catch (Exception ignored){}}
}
