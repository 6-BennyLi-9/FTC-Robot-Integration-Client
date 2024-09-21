package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Templates.TestProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

@TeleOp(name = "ClassicTest",group = "tune")
public class ClassicTest extends TestProgramTemplate {
	public Classic classic;
	public Robot lazyRobot;

	@Override
	public void opInit() {
		lazyRobot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		classic=lazyRobot.classic;
		lazyRobot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void mainCode() {
		classic.operateThroughGamePad(lazyRobot.gamepad);
	}
}
