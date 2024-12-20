package org.firstinspires.ftc.teamcode.hardwares.controllers;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationEncoders;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;

/**
 * 集成化控制所有传感器
 * <p>
 * 不要盲目 updateEncoders()
 *
 * @see IntegrationBNO055
 * @see IntegrationEncoders
 */
public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public IntegrationBNO055   imu;
	public IntegrationEncoders Left,Middle,Right;

	public Sensors(@NonNull final IntegrationHardwareMap hardwareMap){
		this.imu = (IntegrationBNO055) hardwareMap.getDevice(HardwareDeviceTypes.imu);
		this.Left =(IntegrationEncoders) hardwareMap.getDevice(HardwareDeviceTypes.LeftDeadWheel);
		this.Middle =(IntegrationEncoders) hardwareMap.getDevice(HardwareDeviceTypes.MiddleDeadWheel);
		this.Right =(IntegrationEncoders) hardwareMap.getDevice(HardwareDeviceTypes.RightDeadWheel);
	}

	/**
	 * 不要盲目执行该函数，这与定位系统的稳定性有直接关联
	 */
	public void updateEncoders(){
		this.Left.update();
		this.Middle.update();
		this.Right.update();
	}
	/**
	 * 不要盲目执行该函数，这与定位系统的稳定性有直接关联
	 */
	public void updateBNO(){
		this.imu.update();
	}

	public void update(){
		this.updateBNO();
	}

	/**
	 * @return 机器前进的TICK数
	 */
	public double getDeltaA(){
		return (this.Left.deltaEncTicks + this.Right.deltaEncTicks) / 2;
	}
	/**
	 * @return 机器平移的TICK数
	 */
	public double getDeltaL(){
		return this.Middle.deltaEncTicks - Params.AxialPosition * this.getDeltaT();
	}
	/**
	 * @return 机器旋转的TICK数
	 */
	public double getDeltaT(){
		return (this.Right.deltaEncTicks - this.Left.deltaEncTicks) / Params.LateralPosition;
	}

	/**
	 * @return 机器前进的TICK数
	 */
	public double getDeltaAxialInch(){
		return this.getDeltaA() * Params.AxialInchPerTick;
	}
	/**
	 * @return 机器平移的TICK数
	 */
	public double getDeltaLateralInch(){
		return this.getDeltaL() * Params.LateralInchPerTick;
	}
	/**
	 * @return 机器旋转的TICK数
	 */
	public double getDeltaTurningDeg(){
		return this.getDeltaT() * Params.TurningDegPerTick;
	}


	@ExtractedInterfaces
	public double robotAngle() {
		return this.imu.robotAngle;
	}
}
