package org.firstinspires.ftc.teamcode.codes.templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Templates
public abstract class DebugProgramTemplate extends OpMode {
	public Client client;
	public Timer timer;

	@Override
	public void init() {
		client=new Client(telemetry);
		timer=new Timer();
		whenInit();
	}

	@Override
	public void start() {
		timer.stopAndRestart();
	}

	@Override
	public void loop() {
		client.changeData("TPS", timer.restartAndGetDeltaTime() /1000);

		whileActivating();
	}

	public abstract void whileActivating();
	public abstract void whenInit();
}
