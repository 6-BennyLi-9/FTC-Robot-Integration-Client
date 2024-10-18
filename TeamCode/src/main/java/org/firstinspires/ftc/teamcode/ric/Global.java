package org.firstinspires.ftc.teamcode.ric;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ric.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.ric.utils.ActionBox;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;
import org.firstinspires.ftc.teamcode.ric.utils.enums.RunningMode;

public final class Global {
	public static Robot                  robot;
	public static Client                 client;
	public static RunningMode            runMode;
	public static ActionBox              actionBox;
	public static DriverProgram          driverProgram;
	public static IntegrationGamepad     integrationGamepad;
	public static Gamepad                currentGamepad1, currentGamepad2;
	public static IntegrationHardwareMap integrationHardwareMap;

	@UserRequirementFunctions
	public static void clear() {
		robot = null;
		client = null;
		runMode = null;
		actionBox = null;
		driverProgram = null;
		integrationGamepad = null;
		currentGamepad1 = null;
		currentGamepad2 = null;
	}

	public static void setRobot(@NonNull Robot robot) {
		Global.robot = robot;
		Global.driverProgram = robot.drive;
		Global.client = robot.client;
		Global.runMode = robot.runningState;
		Global.actionBox = robot.actionBox;
		integrationHardwareMap=robot.lazyIntegratedDevices;
		if(robot.gamepad!= null) {
			Global.integrationGamepad = robot.gamepad;
			Global.currentGamepad1 = robot.gamepad.gamepad1.gamepad;
			Global.currentGamepad2 = robot.gamepad.gamepad2.gamepad;
		}
	}
}
