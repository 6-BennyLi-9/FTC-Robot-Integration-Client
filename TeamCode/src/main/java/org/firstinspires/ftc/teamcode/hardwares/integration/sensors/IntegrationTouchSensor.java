package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;

public class IntegrationTouchSensor extends IntegrationSensor{
	public final TouchSensor sensor;
	public boolean pressed=false;

	public IntegrationTouchSensor(@NonNull TouchSensor sensor, @NonNull HardwareDeviceTypes deviceType) {
		super(deviceType.deviceName);
		this.sensor=sensor;
	}

	@Override
	public void update() {
		pressed=sensor.isPressed();
	}
}
