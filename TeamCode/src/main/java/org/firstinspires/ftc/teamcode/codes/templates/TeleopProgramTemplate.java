package org.firstinspires.ftc.teamcode.codes.templates;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

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
		this.robot =new Robot(this.hardwareMap, RunningMode.ManualDrive, this.telemetry);
		this.timer =new Timer();
		this.registerGamePad();
		this.whenInit();
	}

	/**
	 * Play 按鍵
	 * 不要二次覆寫
	 */
	@Override
	public void start() {
		this.timer.restart();
		this.robot.addData("TPS","WAIT FOR START");
	}

	/**
	 * 主程序接口
	 * 不要二次覆寫
	 */
	@Override
	public void loop() {
		final double tps = 1000 / this.timer.restartAndGetDeltaTime();
		this.robot.changeData("TPS", tps);
		if(30 > tps){
			Log.w("TPS Waring","Low TPS, Actions might not work well.");
		}

		this.whileActivating();
	}

	@UserRequirementFunctions
	@UtilFunctions
	public void registerGamePad(){
		this.robot.registerGamepad(this.gamepad1, this.gamepad2);
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
