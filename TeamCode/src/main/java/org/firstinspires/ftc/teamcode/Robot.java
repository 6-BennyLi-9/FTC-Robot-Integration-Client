package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DriveControlsAddition.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.Structure;
import org.firstinspires.ftc.teamcode.Hardwares.Webcam;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.enums.ClipPosition;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

import java.util.Objects;

public class Robot {
	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public Classic classic;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PID_processor pidProcessor;

	private SimpleMecanumDrive drive=null;//如果您不想使用我们的drive，那么保留drive的值为null是没问题的

	public Robot(HardwareMap hardwareMap, @NonNull runningState state, Client client){
		if(hardwareMap==null||client==null){
			throw new NullPointerException();
		}
		motors=new Motors(hardwareMap);
		sensors=new Sensors(hardwareMap);
		servos=new Servos(hardwareMap);

		classic=new Classic(motors,sensors);
		structure=new Structure(motors,servos);
		webcam=new Webcam();

		this.client=client;
		pidProcessor=new PID_processor();

		//TODO:如果需要，在这里修改RuntimeOption中的值
		if (Objects.requireNonNull(state) == runningState.Autonomous) {
			InitInAutonomous();
		} else if (state == runningState.ManualDrive) {
			RuntimeOption.runUpdateWhenAnyNewOptionsAdded=true;
			RuntimeOption.driverUsingAxisPowerInsteadOfCurrentPower=false;

			InitInManualDrive();
		} else {
			throw new NullPointerException("Unexpected runningState value???");
		}
	}

	/**
	 * 自动初始化SimpleMecanumDrive
	 * @return 返回定义好的SimpleMecanumDrive
	 */
	public SimpleMecanumDrive enableSimpleMecanumDrive(Pose2d RobotPosition){
		drive=new SimpleMecanumDrive(this,RobotPosition);
		return drive;
	}
	private void InitInAutonomous(){
		structure.ClipOption(ClipPosition.Close);
	}
	private void InitInManualDrive(){
		structure.ClipOption(ClipPosition.Open);
	}

	public void update() throws InterruptedException {
		sensors.update();
		servos.update();
		if(RuntimeOption.driverUsingAxisPowerInsteadOfCurrentPower) {
			motors.update(sensors.FirstAngle);
		}else{
			motors.update();
		}

		while(RuntimeOption.waitForServoUntilThePositionIsInPlace && servos.InPlace()){
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}
	
	public void operateThroughGamePad(Gamepad gamepad1,Gamepad gamepad2){
		classic.operateThroughGamePad(gamepad1);
		structure.operateThroughGamePad(gamepad2);
	}

	/**
	 * 在该节点让机器旋转指定角度
	 * @param angle 要转的角度[-180,180)
	 */
	public void turnAngle(double angle){
		drive.runDriveCommandPackage(drive.drivingCommandsBuilder().TurnAngle(angle).END());
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(double BufPower){
		drive.runDriveCommandPackage(drive.drivingCommandsBuilder().SetPower(BufPower).END());
		motors.setBufPower(BufPower);
	}
}
