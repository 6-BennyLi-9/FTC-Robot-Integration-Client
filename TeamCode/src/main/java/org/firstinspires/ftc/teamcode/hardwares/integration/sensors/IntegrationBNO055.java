package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;

/**
 * 基于 BNO055IMU 的接口性类
 *
 * @see BNO055IMU
 */
public class IntegrationBNO055 extends IntegrationSensor{
	public final BNO055IMU sensor;
	public double robotAngle,xAccel,yAccel;
	public boolean knockedWarn;

	public IntegrationBNO055(@NonNull final BNO055IMU imu, @NonNull final HardwareDeviceTypes deviceType) {
		super(deviceType.deviceName);
		this.sensor = imu;
		final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

		parameters.angleUnit= BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit= BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile="BNO055Calibration.json";
		parameters.loggingEnabled=true;
		parameters.loggingTag="IMU";
		parameters.accelerationIntegrationAlgorithm=new JustLoggingAccelerationIntegrator();
		this.sensor.initialize(parameters);
	}

	@Override
	public void update() {
		this.robotAngle = this.sensor.getAngularOrientation().firstAngle;
		this.xAccel = this.sensor.getGravity().xAccel;
		this.yAccel = this.sensor.getGravity().yAccel;
		this.knockedWarn = 10 < xAccel || 10 < yAccel;
	}
}
