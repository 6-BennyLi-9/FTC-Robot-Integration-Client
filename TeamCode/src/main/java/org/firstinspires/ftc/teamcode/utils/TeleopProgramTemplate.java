package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

public abstract class TeleopProgramTemplate extends OpMode {
	public Robot robot;
	public long st,et;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, runningState.ManualDrive,telemetry);
		whenInit();
	}

	@Override
	public void start() {
		super.start();
		st=System.currentTimeMillis();
		robot.client.addData("TPS","WAIT FOR START");
	}

	@Override
	public void loop() {
		et=System.currentTimeMillis();
		robot.client.changeDate("TPS", (double) (et - st) /1000);
		st=et;

		whileActivating();
	}


	public abstract void whileActivating();
	public abstract void whenInit();
}
