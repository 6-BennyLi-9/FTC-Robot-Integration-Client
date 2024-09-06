package org.firstinspires.ftc.teamcode.RIC_samples.Templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Enums.runningState;
import org.firstinspires.ftc.teamcode.Utils.Timer;

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
		robot.client.changeData("TPS", timer.restartAndGetDeltaTime() /1000);

		whileActivating();
	}


	public abstract void whileActivating();
	public abstract void whenInit();
}
