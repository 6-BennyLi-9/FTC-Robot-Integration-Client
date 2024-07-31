package org.firstinspires.ftc.teamcode.Hardwares;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;

import java.util.Objects;

public class Robot {
	public static final class RuntimeOption{
		public final static boolean autoPrepareForNextOptionWhenUpdate=true;
		public final static boolean clearAllOptionsWhenUpdate=false;
		public final static boolean runUpdateWhenAnyNewOptionsAdded=false;
		public final static boolean waitForServoUntilThePositionIsInPlace=false;
	}

	public enum runningState{
		Autonomous,
		TeleOps
	}


	private final Motors motors;
	private final Sensors sensors;
	private final Servos servos;

	public Classic classic;
	public Structure structure;
	public Robot(HardwareMap hardwareMap,runningState state){
		motors=new Motors(hardwareMap);
		sensors=new Sensors(hardwareMap);
		servos=new Servos(hardwareMap);

		classic=new Classic(motors);
		structure=new Structure(motors,servos);

		if (Objects.requireNonNull(state) == runningState.Autonomous) {
			InitInAutonomous();
		} else if (state == runningState.TeleOps) {
			InitInTeleOps();
		} else {
			throw new NullPointerException("HOW COULD THE STATE EVER BE NULL OR BEEN CHANGED???");
		}
	}

	private void InitInAutonomous(){
		structure.closeClips();
	}
	private void InitInTeleOps(){
		structure.openClips();
	}

	public void update() throws InterruptedException {
		motors.update();
		servos.update();
		sensors.update();

		if(RuntimeOption.autoPrepareForNextOptionWhenUpdate){
			prepareForNewCommands();
		}

		while(RuntimeOption.waitForServoUntilThePositionIsInPlace && servos.InPlace()){
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}
	public void prepareForNewCommands(){
		motors.clearDriveOptions();
	}
}
