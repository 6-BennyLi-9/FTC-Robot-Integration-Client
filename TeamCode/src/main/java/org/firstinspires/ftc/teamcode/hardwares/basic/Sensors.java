package org.firstinspires.ftc.teamcode.hardwares.basic;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationDeadWheelEncoders;
import org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public IntegrationBNO055 imu;
	public IntegrationDeadWheelEncoders Left,Middle,Right;

	public Sensors(@NonNull IntegrationHardwareMap hardwareMap){
		imu= (IntegrationBNO055) hardwareMap.getDevice(HardwareDeviceTypes.imu);
		Left=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDeviceTypes.LeftDeadWheel);
		Middle=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDeviceTypes.MiddleDeadWheel);
		Right=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDeviceTypes.RightDeadWheel);
	}

	public void updateEncoders(){
		Left.update();
		Middle.update();
		Right.update();
	}
	public void update(){
		imu.update();
		updateEncoders();
	}

	/**
	 * @return 机器前进的TICK数
	 */
	@ExtractedInterfaces
	public double getDeltaA(){
		return (Left.deltaEncTicks+Right.deltaEncTicks)/2;
	}
	/**
	 * @return 机器平移的TICK数
	 */
	@ExtractedInterfaces
	public double getDeltaL(){
		return Middle.deltaEncTicks-Params.AxialPosition*getDeltaT();
	}
	/**
	 * @return 机器旋转的TICK数
	 */
	@ExtractedInterfaces
	public double getDeltaT(){
		return (Right.deltaEncTicks-Left.deltaEncTicks)/Params.LateralPosition;
	}

	@ExtractedInterfaces
	public double robotAngle() {
		return imu.robotAngle;
	}
}