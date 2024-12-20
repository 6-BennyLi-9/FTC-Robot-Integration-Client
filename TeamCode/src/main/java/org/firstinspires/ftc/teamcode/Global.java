package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.utils.ActionBox;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

/**
 * 用于优化类之间的数据传递
 * <p>
 * 主要防止内存泄漏
 */
public enum Global {
	;
	public static Robot                  robot;
	public static Client                 client;
	public static ActionBox              actionBox;
	public static DriverProgram          driverProgram;
	public static IntegrationGamepad     integrationGamepad;
	public static Gamepad                currentGamepad1, currentGamepad2;
	public static IntegrationHardwareMap integrationHardwareMap;

	@UserRequirementFunctions
	public static void clear() {
		robot = null;
		client = null;
		actionBox = null;
		driverProgram = null;
		integrationGamepad = null;
		currentGamepad1 = null;
		currentGamepad2 = null;
	}

	public static void setRobot(@NonNull final Robot robot) {
		Global.robot = robot;
		driverProgram = robot.drive;
		client = robot.client;
		actionBox = robot.actionBox;
		integrationHardwareMap =robot.lazyIntegratedDevices;
		if(null != robot.gamepad) {
			integrationGamepad = robot.gamepad;
			currentGamepad1 = robot.gamepad.gamepad1.gamepad;
			currentGamepad2 = robot.gamepad.gamepad2.gamepad;
		}
	}
}
