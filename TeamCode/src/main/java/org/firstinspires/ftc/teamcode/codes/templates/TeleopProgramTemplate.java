package org.firstinspires.ftc.teamcode.codes.templates;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Templates
public abstract class TeleopProgramTemplate extends OpMode {
	public Robot robot;
	public Timer timer;

	/**
	 * INIT 按鍵
	 * 不要二次覆寫
	 */
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.ManualDrive,telemetry);
		timer=new Timer();
		registerGamePad();
		whenInit();
	}

	/**
	 * Play 按鍵
	 * 不要二次覆寫
	 */
	@Override
	public void start() {
		timer.restart();
		robot.addData("TPS","WAIT FOR START");
	}

	/**
	 * 主程序接口
	 * 不要二次覆寫
	 */
	@Override
	public void loop() {
		double tps=1000/timer.restartAndGetDeltaTime();
		robot.changeData("TPS", tps);
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

	/**
	 * 初始化程序
	 * @see #init()
	 */
	public abstract void whenInit();

	/**
	 * 主程序
	 * @see #loop()
	 */
	public abstract void whileActivating();
}
