package org.firstinspires.ftc.teamcode.codes.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.codes.codes.templates.TestProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

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
