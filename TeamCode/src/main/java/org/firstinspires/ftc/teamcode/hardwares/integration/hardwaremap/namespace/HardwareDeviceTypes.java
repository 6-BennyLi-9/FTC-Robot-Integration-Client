package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace;

import static org.firstinspires.ftc.teamcode.Params.HardwareNamespace.*;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareState.*;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 可以自动登记硬件的名字及其类型
 * @see Params.HardwareNamespace
 */
public enum HardwareDeviceTypes {
	LeftFront       (-1, Params.HardwareNamespace.LeftFront, DcMotorEx.class,DeviceConfigPackage.Direction.Reversed),
	RightFront      (-1, Params.HardwareNamespace.RightFront, DcMotorEx.class),
	LeftRear        (-1, Params.HardwareNamespace.LeftRear, DcMotorEx.class,DeviceConfigPackage.Direction.Reversed),
	RightRear       (-1, Params.HardwareNamespace.RightRear, DcMotorEx.class),
	PlacementArm    (-1, Params.HardwareNamespace.PlacementArm, DcMotorEx.class, Disabled),
	Intake          (-1, Params.HardwareNamespace.Intake, DcMotorEx.class, Disabled),
	FrontClip       (-1, Params.HardwareNamespace.FrontClip, Servo.class, Disabled),
	RearClip        (-1, Params.HardwareNamespace.RearClip, Servo.class, Disabled),
	SuspensionArm   (-1, Params.HardwareNamespace.SuspensionArm, DcMotorEx.class, Disabled),
	LeftDeadWheel   (-1, LeftRear.deviceName, DcMotorEx.class, DeviceConfigPackage.Direction.Reversed),
	MiddleDeadWheel (-1, LeftFront.deviceName,DcMotorEx.class, DeviceConfigPackage.Direction.Reversed),
	RightDeadWheel  (-1, RightFront.deviceName, DcMotorEx.class, DeviceConfigPackage.Direction.Reversed),
	imu             (-1, Imu, BNO055IMU.class);
	public final String deviceName;
	public final Class<?> classType;
	public final DeviceConfigPackage config;
	public final int teamCode;
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType, final DeviceConfigPackage config){
		this.config=config;
		this.classType=classType;
		this.deviceName=deviceName;
		this.teamCode=teamCode;
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AutoComplete());
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType, final HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType, final DeviceConfigPackage.Direction direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType, final HardwareState state, final DeviceConfigPackage.Direction direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final int teamCode, final String deviceName, final Class<?> classType, final DeviceConfigPackage.Direction direction, final HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
}