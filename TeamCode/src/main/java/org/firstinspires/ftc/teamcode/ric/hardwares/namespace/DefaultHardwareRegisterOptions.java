package org.firstinspires.ftc.teamcode.ric.hardwares.namespace;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationHardwareMap;

public class DefaultHardwareRegisterOptions implements CustomizedHardwareRegisterOptions{
	@Override
	public void run(@NonNull IntegrationHardwareMap map) {
		map.registerAllDevices();
	}
}
