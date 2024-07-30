package org.firstinspires.ftc.teamcode.Hardwares;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;

public class Robot {
	private final Motors motors;
	private final Sensors sensors;
	private final Servos servos;

	public Classic classic;
	public Structure structure;
	public Robot(HardwareMap hardwareMap){
		motors=new Motors(hardwareMap);
		sensors=new Sensors(hardwareMap);
		servos=new Servos(hardwareMap);

		classic=new Classic(motors);
		structure=new Structure(motors,servos);
	}

	public void InitInAutonomous(){
		structure.closeClips();
	}
	public void InitInTeleOps(){
		structure.openClips();
	}

	public void update(){
		motors.update();
		servos.update();
		sensors.update();
	}
	public void prepareForNewCommands(){
		motors.clearDriveOptions();
	}
}
