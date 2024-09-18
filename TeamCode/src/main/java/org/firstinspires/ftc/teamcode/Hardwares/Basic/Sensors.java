package org.firstinspires.ftc.teamcode.Hardwares.Basic;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationDeadWheelEncoders;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public IntegrationBNO055 imu;
	public IntegrationDeadWheelEncoders Left,Middle,Right;

	public Sensors(@NonNull IntegrationHardwareMap hardwareMap){
		imu=(IntegrationBNO055) hardwareMap.getDevice(HardwareDevices.imu);
		Left=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDevices.LeftDeadWheel);
		Middle=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDevices.MiddleDeadWheel);
		Right=(IntegrationDeadWheelEncoders) hardwareMap.getDevice(HardwareDevices.RightDeadWheel);
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

	@ExtractedInterfaces
	public double RobotAngle() {
		return imu.RobotAngle;
	}
}
