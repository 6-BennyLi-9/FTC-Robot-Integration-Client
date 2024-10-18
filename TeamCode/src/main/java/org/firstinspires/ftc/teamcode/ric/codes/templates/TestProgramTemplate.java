package org.firstinspires.ftc.teamcode.ric.codes.templates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ric.Global;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;

@Templates
public abstract class TestProgramTemplate extends LinearOpMode {
	public Client client;

	@Override
	public void runOpMode() throws InterruptedException {
		Global.clear();
		client=new Client(telemetry);
		opInit();


		while(!opModeIsActive()&&!isStopRequested()){
			sleep(500);
		}

		if(isStopRequested())return;

		mainCode();
		sleep(1145141919);
	}

	public abstract void opInit();
	public abstract void mainCode();
}
