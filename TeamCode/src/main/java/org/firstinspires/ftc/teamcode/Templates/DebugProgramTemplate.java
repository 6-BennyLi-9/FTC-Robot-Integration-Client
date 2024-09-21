package org.firstinspires.ftc.teamcode.Templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Timer;

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
