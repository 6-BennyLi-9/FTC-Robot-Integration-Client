package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardwares.namespace.RobotDevices;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public BNO055IMU imu;
	public double FirstAngle,XMoved,YMoved;

	public Sensors(@NonNull HardwareMap hardwareMap){
		imu=hardwareMap.get(BNO055IMU.class,RobotDevices.imu.name);

		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit= BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit= BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile="BNO055Calibration.json";
		parameters.loggingEnabled=true;
		parameters.loggingTag="IMU";
		parameters.accelerationIntegrationAlgorithm=new JustLoggingAccelerationIntegrator();
		imu.initialize(parameters);
	}

	public void update(){
		FirstAngle=imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
		XMoved=imu.getPosition().x;
		YMoved=imu.getPosition().y;
	}
}
