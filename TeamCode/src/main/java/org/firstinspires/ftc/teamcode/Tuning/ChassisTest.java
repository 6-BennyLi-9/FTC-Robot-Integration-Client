package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardwares.Chassis;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Templates.TestProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

@TeleOp(name = "ChassisTest",group = "tune")
public class ChassisTest extends TestProgramTemplate {
	public Chassis chassis;
	public Robot lazyRobot;

	@Override
	public void opInit() {
		lazyRobot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		chassis =lazyRobot.chassis;
		lazyRobot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void mainCode() {
		chassis.operateThroughGamePad(lazyRobot.gamepad);
	}
}
