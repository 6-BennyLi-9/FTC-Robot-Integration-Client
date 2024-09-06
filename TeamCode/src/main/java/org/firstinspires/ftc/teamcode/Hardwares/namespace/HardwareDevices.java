package org.firstinspires.ftc.teamcode.Hardwares.namespace;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params.namespace;
import org.firstinspires.ftc.teamcode.Utils.DeviceConfigPackage;
import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;

/**
 * 可以自动登记硬件的名字及其类型
 * @see namespace
 */
public enum HardwareDevices {
	LeftFront(namespace.LeftFront, DcMotorEx.class),
	RightFront(namespace.RightFront, DcMotorEx.class),
	LeftRear(namespace.LeftRear, DcMotorEx.class),
	RightRear(namespace.RightRear, DcMotorEx.class),
	PlacementArm(namespace.PlacementArm, DcMotorEx.class,HardwareState.Disabled),
	Intake(namespace.Intake, DcMotorEx.class,HardwareState.Disabled),
	FrontClip(namespace.FrontClip, Servo.class,HardwareState.Disabled),
	RearClip(namespace.RearClip, Servo.class,HardwareState.Disabled),
	SuspensionArm(namespace.SuspensionArm, DcMotorEx.class,HardwareState.Disabled),
	LeftDeadWheel(LeftRear.deviceName, DcMotorEx.class),
	MiddleDeadWheel(LeftFront.deviceName,DcMotorEx.class),
	RightDeadWheel(RightFront.deviceName, DcMotorEx.class),
	imu(namespace.Imu, BNO055IMU .class);
	public final String deviceName;
	public final Class<?> classType;
	public final DeviceConfigPackage config;

	HardwareDevices(String deviceName, Class<?> classType,DeviceConfigPackage config){
		this.config=config;
		this.classType=classType;
		this.deviceName=deviceName;
	}
	HardwareDevices(String deviceName, Class<?> classType){
		this(deviceName,classType,new DeviceConfigPackage().AutoComplete());
	}
	HardwareDevices(String deviceName, Class<?> classType,HardwareState state){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(state));
	}
	HardwareDevices(String deviceName, Class<?> classType, Direction direction){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction));
	}
	HardwareDevices(String deviceName, Class<?> classType,HardwareState state, Direction direction){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
	HardwareDevices(String deviceName, Class<?> classType, Direction direction,HardwareState state){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
}