package org.firstinspires.ftc.teamcode.RIC_samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningState;
import org.firstinspires.ftc.teamcode.Utils.Timer;

@TeleOp(name = "ManualCodeSample",group = "samples")
public class ManualCodeSample extends OpMode {
	public Robot robot;
	public Timer timer;

	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningState.ManualDrive,telemetry);
		robot.client.addData("TPS","NEED TO START THE OpMode TO SEE THE VALUE.");
		timer=new Timer();
	}

	@Override
	public void loop() {
		updateTPS();

		robot.operateThroughGamePad(gamepad1,gamepad2);
		robot.update();
	}

	public void updateTPS(){
		robot.client.changeData("TPS", 1000/(timer.restartAndGetDeltaTime()));
	}
}
