package org.firstinspires.ftc.teamcode.codes.samples.exceptionSamples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@Autonomous(name = "BasicsExceptionSample",group = Params.Configs.SampleOpModesGroup)
public class BasicsExceptionSample extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {

	}

	/**
	 * 由于没有执行对 gamepad 的登记，因此为空
	 *
	 * @see Robot#registerGamepad(Gamepad, Gamepad)
	 */
	@UserRequirementFunctions
	public void nullPointerException1(){
		Robot robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed);
	}

	/**
	 * 使用 SimpleMecanumDrive 时务必提前定义 Robot
	 * <p>
	 * 你真的认证看我写的注释了吗？
	 *
	 * @see SimpleMecanumDrive
	 * @see Robot
	 */
	@UserRequirementFunctions
	public void nullPointerException2(){
		SimpleMecanumDrive drive=new SimpleMecanumDrive(new Position2d(0,0,0));
		drive.drivingCommandsBuilder().END();
	}
}
