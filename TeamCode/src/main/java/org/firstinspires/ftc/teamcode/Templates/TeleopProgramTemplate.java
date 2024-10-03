package org.firstinspires.ftc.teamcode.Templates;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Annotations.Templates;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;
import org.firstinspires.ftc.teamcode.Utils.Timer;

@Templates
public abstract class TeleopProgramTemplate extends OpMode {
	public Robot robot;
	public Timer timer;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.ManualDrive,telemetry);
		timer=new Timer();
		whenInit();
	}

	@Override
	public void start() {
		timer.restart();
		robot.client.addData("TPS","WAIT FOR START");
	}

	@Override
	public void loop() {
		double tps=timer.restartAndGetDeltaTime() /1000;
		robot.client.changeData("TPS", tps);
		if(tps<30){
			Log.w("TPS Waring","Low TPS, Actions might not work well.");
		}

		whileActivating();
	}

	@UserRequirementFunctions
	@UtilFunctions
	public void registerGamePad(){
		robot.registerGamepad(gamepad1,gamepad2);
	}

	public abstract void whileActivating();
	public abstract void whenInit();
}
