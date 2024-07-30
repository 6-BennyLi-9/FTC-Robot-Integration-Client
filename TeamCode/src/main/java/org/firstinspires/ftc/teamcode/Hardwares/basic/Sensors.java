package org.firstinspires.ftc.teamcode.Hardwares.basic;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardwares.hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public BNO055IMU imu;

	public Sensors(HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace=new namespace();
		imu=hardwareMap.get(BNO055IMU.class,namespace.Hardware.get(hardware.imu));
	}
}
