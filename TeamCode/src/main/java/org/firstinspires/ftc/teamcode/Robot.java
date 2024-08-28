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
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive.drivingCommandsBuilder;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.Structure;
import org.firstinspires.ftc.teamcode.Hardwares.Webcam;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.Hardwares.namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.Enums.ClipPosition;
import org.firstinspires.ftc.teamcode.utils.Enums.State;
import org.firstinspires.ftc.teamcode.utils.Enums.runningState;
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.Timer;

import java.util.Objects;

public class Robot {
	public DeviceMap devices;

	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public Classic classic;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PID_processor pidProcessor;

	public State state;
	public runningState RunningState;
	private SimpleMecanumDrive drive=null;

	public Timer timer;

	public Robot(@NonNull HardwareMap hardwareMap, @NonNull runningState state, @NonNull Telemetry telemetry){
		this(hardwareMap,state,new Client(telemetry));
	}
	public Robot(@NonNull HardwareMap hardwareMap, @NonNull runningState state, @NonNull Client client){
		devices=new DeviceMap(hardwareMap);

		motors=new Motors(devices);
		sensors=new Sensors(devices);
		servos=new Servos(devices);

		classic=new Classic(motors,sensors);
		structure=new Structure(motors,servos);
		webcam=new Webcam(hardwareMap);

		this.client=client;
		pidProcessor=new PID_processor();

		//TODO:如果需要，在这里修改RuntimeOption中的值
		if (Objects.requireNonNull(state) == runningState.Autonomous) {
			InitInAutonomous();
		} else if (state == runningState.ManualDrive) {
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
	public SimpleMecanumDrive InitMecanumDrive(Pose2d RobotPosition){
		drive=new SimpleMecanumDrive(this,RobotPosition);
		if(RunningState!=runningState.Autonomous) {
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
			motors.update(sensors.FirstAngle);
		}else{
			motors.update();
		}

		client.changeDate("State",state.name());
		while(Params.Configs.waitForServoUntilThePositionIsInPlace && servos.InPlace()){
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}
	
	public void operateThroughGamePad(Gamepad gamepad1,Gamepad gamepad2){
		classic.operateThroughGamePad(gamepad1);
		structure.operateThroughGamePad(gamepad2);
	}
	public drivingCommandsBuilder drivingCommandsBuilder(){
		return drive.drivingCommandsBuilder();
	}

	/**
	 * 在该节点让机器旋转指定角度
	 * @param angle 要转的角度[-180,180)
	 */
	public void turnAngle(double angle){
		if(RunningState==runningState.ManualDrive)return;
		drive.runDriveCommandPackage(drive.drivingCommandsBuilder().TurnAngle(angle).END());
	}
	public void strafeTo(Vector2d pose){
		if(RunningState==runningState.ManualDrive)return;
		drive.runDriveCommandPackage(drive.drivingCommandsBuilder().StrafeTo(pose).END());
	}

	public Pose2d pose(){
		drive.update();
		return drive.RobotPosition;
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(double BufPower){
		if(drive!=null) {
			drive.runDriveCommandPackage(drive.drivingCommandsBuilder().SetPower(BufPower).END());//考虑是否删去此代码片段
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
	public void deleteDate(String key){try{client.deleteDate(key);}catch (Exception ignored){}}
	@ExtractedInterfaces
	public void changeData(String key, String val){client.changeDate(key, val);}
	@ExtractedInterfaces
	public void changeData(String key,int val){client.changeDate(key, val);}
	@ExtractedInterfaces
	public void changeData(String key,double val){client.changeDate(key, val);}
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
