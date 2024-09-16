package org.firstinspires.ftc.teamcode.Hardwares.Basic;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationDeadWheelEncoders;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Enums.DeadWheelsType;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public IntegrationBNO055 imu;
	//TODO:按需求修改
	public final DeadWheelsType DeadWheelType =DeadWheelsType.ThreeDeadWheels;
	public IntegrationDeadWheelEncoders Left,Middle,Right;

	public Sensors(@NonNull DeviceMap deviceMap){
		imu=new IntegrationBNO055(deviceMap,HardwareDevices.imu);
		Left=new IntegrationDeadWheelEncoders(deviceMap,HardwareDevices.LeftDeadWheel);
		Middle =new IntegrationDeadWheelEncoders(deviceMap,HardwareDevices.MiddleDeadWheel);
		Right=new IntegrationDeadWheelEncoders(deviceMap,HardwareDevices.RightDeadWheel);
	}

	public void update(){
		imu.update();
		Left.update();
		Middle.update();
		Right.update();
	}

	/**
	 * @return 机器前进的TICK数
	 */
	public double getDeltaA(){
		return (Left.deltaEncTicks+Right.deltaEncTicks)/2;
	}
	/**
	 * @return 机器平移的TICK数
	 */
	public double getDeltaL(){
		return Middle.deltaEncTicks-Params.AxialPosition*getDeltaT();
	}
	/**
	 * @return 机器旋转的TICK数
	 */
	public double getDeltaT(){
		return (Right.deltaEncTicks-Left.deltaEncTicks)/Params.LateralPosition;
	}
}
