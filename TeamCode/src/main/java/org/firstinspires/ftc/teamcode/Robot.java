package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
	private final Motors motors;
	private final Sensors sensors;
	private final Servos servos;

	public Classic classic;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PID_processor pidProcessor;

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
}
