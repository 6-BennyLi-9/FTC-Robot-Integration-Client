package org.firstinspires.ftc.teamcode.ric.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes;

public class IntegrationBNO055 extends IntegrationSensor{
	public final BNO055IMU sensor;
	public double robotAngle,xAccel,yAccel;
	public boolean knockedWarn=false;

	public IntegrationBNO055(@NonNull BNO055IMU imu, @NonNull HardwareDeviceTypes deviceType) {
		super(deviceType.deviceName);
		sensor= imu;
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

		parameters.angleUnit= BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit= BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile="BNO055Calibration.json";
		parameters.loggingEnabled=true;
		parameters.loggingTag="IMU";
		parameters.accelerationIntegrationAlgorithm=new JustLoggingAccelerationIntegrator();
		sensor.initialize(parameters);
	}

	@Override
	public void update() {
		robotAngle =sensor.getAngularOrientation().firstAngle;
		xAccel=sensor.getGravity().xAccel;
		yAccel=sensor.getGravity().yAccel;
		knockedWarn= xAccel > 10 || yAccel > 10;
	}
}
