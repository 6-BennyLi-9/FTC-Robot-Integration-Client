package org.firstinspires.ftc.teamcode.RIC_samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

@TeleOp(name = "ManualCodeSample",group = "samples")
public class ManualCodeSample extends OpMode {
	public Robot robot;
	public double st,et;

	@Override
	public void init() {
		robot=new Robot(hardwareMap, runningState.ManualDrive,telemetry);
		st=System.currentTimeMillis();
		robot.client.addData("TPS","NEED TO START THE OpMode TO SEE THE VALUE.");
	}

	@Override
	public void loop() {
		updateTPS();

		robot.operateThroughGamePad(gamepad1,gamepad2);
		robot.update();
	}

	public void updateTPS(){
		et=System.currentTimeMillis();
		robot.client.changeDate("TPS", String.valueOf(1000/(et-st)));
		st=System.currentTimeMillis();
	}
}
