package org.firstinspires.ftc.teamcode.Hardwares.Namespace;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.IntegrationHardwareMap;

public class DefaultHardwareRegisterOptions implements CustomizedHardwareRegisterOptions{
	@Override
	public void run(@NonNull IntegrationHardwareMap map) {
		map.registerAllDevices();
	}
}
