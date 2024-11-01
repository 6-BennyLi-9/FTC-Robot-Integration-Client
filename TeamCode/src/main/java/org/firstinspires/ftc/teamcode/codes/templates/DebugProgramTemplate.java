package org.firstinspires.ftc.teamcode.codes.templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@Templates
public abstract class DebugProgramTemplate extends OpMode {
	public Client client;
	public Timer timer;

	@Override
	public void init() {
		Global.clear();
		this.client =new Client(this.telemetry);
		this.timer =new Timer();
		this.whenInit();
	}

	@Override
	public void start() {
		this.timer.stopAndRestart();
	}

	@Override
	public void loop() {
		this.client.changeData("TPS", this.timer.restartAndGetDeltaTime() / 1000);

		this.whileActivating();
	}

	public abstract void whileActivating();
	public abstract void whenInit();
}
