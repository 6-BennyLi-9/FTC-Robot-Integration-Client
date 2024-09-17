package org.firstinspires.ftc.teamcode.Samples;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleTeleOPs;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

@TuningOrSampleTeleOPs(name = "ClientUsage",group = "sample")
public class ClientUsage extends OpMode {
	Client client;

	private int time;

	private void waitForXPressed(){
		while(!gamepad1.x){
			Actions.runBlocking(new SleepAction(0.05));
		}
	}

	@Override
	public void init() {
		client=new Client(telemetry);
		time=0;
	}
	private void operateThroughTime(){
		switch ( time ){
			case 1:
				client.addData("按下次数","1");
				break;
			case 2:
				client.changeData("按下次数","2");
				break;
			case 3:
				client.addLine("第三次按下");
				client.deleteData("按下次数");
				break;
			case 4:
				client.deleteLine("第三次按下");
				client.addData("按下次数","4");
				break;
			case 5:
				client.addLine("第五次按下");
				client.deleteData("按下次数");
				break;
			case 6:
				client.changeLine("第五次按下","第六次按下");
				client.changeData("按下次数","6");
				break;
			case 7:
				client.clear();
				client.addLine("演示结束");
				break;
			default:
				client.clear();
				break;
		}
	}

	@Override
	public void loop() {
		++time;
		waitForXPressed();
		operateThroughTime();

		Actions.runBlocking(new SleepAction(0.5));
	}
}