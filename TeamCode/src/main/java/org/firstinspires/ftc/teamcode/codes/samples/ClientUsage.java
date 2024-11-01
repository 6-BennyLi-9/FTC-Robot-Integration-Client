package org.firstinspires.ftc.teamcode.codes.samples;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@TeleOp(name = "ClientUsage",group = Params.Configs.SampleOpModesGroup)
@Disabled
public class ClientUsage extends OpMode {
	Client client;

	private int time;

	private void waitForXPressed(){
		while(! this.gamepad1.x){
			Actions.runBlocking(new SleepAction(0.05));
		}
	}

	@Override
	public void init() {
		this.client =new Client(this.telemetry);
		this.time =0;
	}
	private void operateThroughTime(){
		switch (this.time){
			case 1:
				this.client.addData("按下次数","1");
				break;
			case 2:
				this.client.changeData("按下次数","2");
				break;
			case 3:
				this.client.addLine("第三次按下");
				this.client.deleteData("按下次数");
				break;
			case 4:
				this.client.deleteLine("第三次按下");
				this.client.addData("按下次数","4");
				break;
			case 5:
				this.client.addLine("第五次按下");
				this.client.deleteData("按下次数");
				break;
			case 6:
				this.client.changeLine("第五次按下","第六次按下");
				this.client.changeData("按下次数","6");
				break;
			case 7:
				this.client.clear();
				this.client.addLine("演示结束");
				break;
			default:
				this.client.clear();
				break;
		}
	}

	@Override
	public void loop() {
		++ this.time;
		this.waitForXPressed();
		this.operateThroughTime();

		Actions.runBlocking(new SleepAction(0.5));
	}
}
