package org.firstinspires.ftc.teamcode.ric.codes.templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ric.Global;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

@Templates
public abstract class DebugProgramTemplate extends OpMode {
	public Client client;
	public Timer timer;

	@Override
	public void init() {
		Global.clear();
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
