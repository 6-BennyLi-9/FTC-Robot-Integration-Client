package org.firstinspires.ftc.teamcode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveControls.MecanumDrive;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriverProgram;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.Hardwares.Structure;
import org.firstinspires.ftc.teamcode.Hardwares.Webcam;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Servos;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Enums.ClipPosition;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningStateType;
import org.firstinspires.ftc.teamcode.Utils.Enums.State;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.Utils.Timer;

import java.util.Objects;

public class Robot {
	public IntegrationHardwareMap lazyIntegratedDevices;

	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public Classic classic;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PidProcessor pidProcessor;

	public State state;
	public RunningStateType RunningState;
	private DriverProgram drive=null;

	public Timer timer;

	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningStateType state, @NonNull Telemetry telemetry){
		this(hardwareMap,state,new Client(telemetry));
	}
	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningStateType state, @NonNull Client client){
		lazyIntegratedDevices=new IntegrationHardwareMap(hardwareMap,pidProcessor);

		motors=new Motors(lazyIntegratedDevices);
		sensors=new Sensors(lazyIntegratedDevices);
		servos=new Servos(lazyIntegratedDevices);

		classic=new Classic(motors,sensors);
		structure=new Structure(motors,servos);
		webcam=new Webcam(hardwareMap);

		this.client=client;
		pidProcessor=new PidProcessor();

		//TODO:如果需要，在这里修改RuntimeOption中的值
		if (Objects.requireNonNull(state) == RunningStateType.Autonomous) {
			InitInAutonomous();
		} else if (state == RunningStateType.ManualDrive) {
			Params.Configs.runUpdateWhenAnyNewOptionsAdded=true;
			Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=false;

			InitInManualDrive();
		} else {
			throw new NullPointerException("Unexpected runningState value???");
		}

		RunningState=state;
		timer=new Timer();
		client.addData("State","UnKnow");
	}

	/**
	 * 自动初始化SimpleMecanumDrive
	 * @return 返回定义好的SimpleMecanumDrive
	 */
	public DriverProgram InitMecanumDrive(Pose2d RobotPosition){
		drive=new SimpleMecanumDrive(this,RobotPosition);
		if(RunningState!= RunningStateType.Autonomous) {
			Log.w("Robot.java","Initialized Driving Program in Manual Driving State.");
		}
		return drive;
	}
	private void InitInAutonomous(){
		structure.ClipOption(ClipPosition.Close);
		state= State.IDLE;
		SetGlobalBufPower(0.9f);
	}
	private void InitInManualDrive(){
		structure.ClipOption(ClipPosition.Open);
		state= State.ManualDriving;
		SetGlobalBufPower(0.9f);
	}

	public void update()  {
		if(timer.stopAndGetDeltaTime()>=90000){
			state=State.FinalState;
		}

		sensors.update();
		servos.update();

		if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower) {
			motors.update(sensors.RobotAngle());
		}else{
			motors.update();
		}

		client.changeData("State",state.name());
		while(Params.Configs.waitForServoUntilThePositionIsInPlace && servos.InPlace()){
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}
	
	public void operateThroughGamePad(Gamepad gamepad1,Gamepad gamepad2){
		classic.operateThroughGamePad(gamepad1);
		structure.operateThroughGamePad(gamepad2);
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
		if(RunningState== RunningStateType.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().TurnAngle(angle).END());
	}
	public void strafeTo(Vector2d pose){
		if(RunningState== RunningStateType.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().StrafeTo(pose).END());
	}

	public Pose2d pose(){
		drive.update();
		return drive.getCurrentPose();
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(double BufPower){
		if(drive!=null) {
			drive.runOrderPackage(DrivingOrderBuilder().SetPower(BufPower).END());//考虑是否删去此代码片段
		}
		motors.setBufPower(BufPower);
	}

	@ExtractedInterfaces
	public void addData(String key, String val){client.addData(key, val);}
	@ExtractedInterfaces
	public void addData(String key,int val){client.addData(key, val);}
	@ExtractedInterfaces
	public void addData(String key,double val){client.addData(key, val);}
	@ExtractedInterfaces
	public void deleteDate(String key){try{client.deleteData(key);}catch (Exception ignored){}}
	@ExtractedInterfaces
	public void changeData(String key, String val){client.changeData(key, val);}
	@ExtractedInterfaces
	public void changeData(String key,int val){client.changeData(key, val);}
	@ExtractedInterfaces
	public void changeData(String key,double val){client.changeData(key, val);}
	@ExtractedInterfaces
	public void addLine(String val){client.addLine(val);}
	@ExtractedInterfaces
	public void addLine(int val){client.addLine(val);}
	@ExtractedInterfaces
	public void addLine(double val){client.addLine(val);}
	@ExtractedInterfaces
	public void changeLine(@NonNull Object key, @NonNull Object val){client.changeLine(key.toString(),val.toString());}
	@ExtractedInterfaces
	public void deleteLine(String key){try{client.deleteLine(key);}catch (Exception ignored){}}
}
