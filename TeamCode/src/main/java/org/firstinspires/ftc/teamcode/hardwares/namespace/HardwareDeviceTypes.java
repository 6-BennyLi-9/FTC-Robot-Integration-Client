package org.firstinspires.ftc.teamcode.hardwares.namespace;


import static org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareState.Disabled;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params.HardwareNamespace;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.hardwares.namespace.DeviceConfigPackage.Direction;

/**
 * 可以自动登记硬件的名字及其类型
 * @see HardwareNamespace
 */
public enum HardwareDeviceTypes {
	LeftFront       (-1, HardwareNamespace.LeftFront, DcMotorEx.class),
	RightFront      (-1, HardwareNamespace.RightFront, DcMotorEx.class),
	LeftRear        (-1, HardwareNamespace.LeftRear, DcMotorEx.class),
	RightRear       (-1, HardwareNamespace.RightRear, DcMotorEx.class),
	PlacementArm    (-1, HardwareNamespace.PlacementArm, DcMotorEx.class, Disabled),
	Intake          (-1, HardwareNamespace.Intake, DcMotorEx.class, Disabled),
	FrontClip       (-1, HardwareNamespace.FrontClip, Servo.class, Disabled),
	RearClip        (-1, HardwareNamespace.RearClip, Servo.class, Disabled),
	SuspensionArm   (-1, HardwareNamespace.SuspensionArm, DcMotorEx.class, Disabled),
	LeftDeadWheel   (-1,LeftRear.deviceName, DcMotorEx.class, Direction.Reversed),
	MiddleDeadWheel (-1,LeftFront.deviceName,DcMotorEx.class, Direction.Reversed),
	RightDeadWheel  (-1,RightFront.deviceName, DcMotorEx.class, Direction.Reversed),
	imu             (-1, HardwareNamespace.Imu, BNO055IMU.class);
	public final String deviceName;
	public final Class<?> classType;
	public final DeviceConfigPackage config;
	public final int teamCode;
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType, DeviceConfigPackage config){
		this.config=config;
		this.classType=classType;
		this.deviceName=deviceName;
		this.teamCode=teamCode;
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AutoComplete());
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType, HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType, Direction direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType, HardwareState state, Direction direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(int teamCode, String deviceName, Class<?> classType, Direction direction, HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
}