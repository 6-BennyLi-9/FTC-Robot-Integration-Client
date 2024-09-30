package org.firstinspires.ftc.teamcode.Templates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utils.Annotations.Templates;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

@Templates
public abstract class TestProgramTemplate extends LinearOpMode {
	public Client client;

	@Override
	public void runOpMode() throws InterruptedException {
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
