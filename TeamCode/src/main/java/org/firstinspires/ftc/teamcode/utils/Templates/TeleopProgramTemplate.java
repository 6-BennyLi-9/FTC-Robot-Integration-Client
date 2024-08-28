package org.firstinspires.ftc.teamcode.utils.Templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Enums.runningState;
import org.firstinspires.ftc.teamcode.utils.Timer;

public abstract class TeleopProgramTemplate extends OpMode {
	public Robot robot;
	public Timer timer;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, runningState.ManualDrive,telemetry);
		timer=new Timer();
		whenInit();
	}

	@Override
	public void start() {
		super.start();
		timer.restart();
		robot.client.addData("TPS","WAIT FOR START");
	}

	@Override
	public void loop() {
		robot.client.changeDate("TPS", timer.restartAndGetDeltaTime() /1000);

		whileActivating();
	}


	public abstract void whileActivating();
	public abstract void whenInit();
}
