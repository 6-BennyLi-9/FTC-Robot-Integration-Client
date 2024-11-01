package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;

public class IntegrationTouchSensor extends IntegrationSensor{
	public final TouchSensor sensor;
	public boolean pressed;

	public IntegrationTouchSensor(@NonNull final TouchSensor sensor, @NonNull final HardwareDeviceTypes deviceType) {
		super(deviceType.deviceName);
		this.sensor=sensor;
	}

	@Override
	public void update() {
		this.pressed = this.sensor.isPressed();
	}
}
