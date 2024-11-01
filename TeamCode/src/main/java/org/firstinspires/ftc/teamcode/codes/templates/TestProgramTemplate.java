package org.firstinspires.ftc.teamcode.codes.templates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@Templates
public abstract class TestProgramTemplate extends LinearOpMode {
	public Client client;

	@Override
	public void runOpMode() {
		Global.clear();
		this.client =new Client(this.telemetry);
		this.opInit();


		while(! this.opModeIsActive() && ! this.isStopRequested()){
			this.sleep(500);
		}

		if(this.isStopRequested())return;

		this.mainCode();
		this.sleep(1145141919);
	}

	public abstract void opInit();
	public abstract void mainCode();
}
