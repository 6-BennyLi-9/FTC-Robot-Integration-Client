package core.Hardwares.namespace;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import static core.Utils.Enums.HardwareState.Disabled;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Servo;

import core.Params.namespace;
import core.Utils.Annotations.UserRequirementFunctions;
import core.Utils.DeviceConfigPackage;
import core.Utils.Enums.HardwareState;

/**
 * 可以自动登记硬件的名字及其类型
 * @see namespace
 */
public enum HardwareDevices {
	LeftFront(namespace.LeftFront, DcMotorEx.class),
	RightFront(namespace.RightFront, DcMotorEx.class),
	LeftRear(namespace.LeftRear, DcMotorEx.class),
	RightRear(namespace.RightRear, DcMotorEx.class),
	PlacementArm(namespace.PlacementArm, DcMotorEx.class, Disabled),
	Intake(namespace.Intake, DcMotorEx.class, Disabled),
	FrontClip(namespace.FrontClip, Servo.class, Disabled),
	RearClip(namespace.RearClip, Servo.class, Disabled),
	SuspensionArm(namespace.SuspensionArm, DcMotorEx.class, Disabled),
	LeftDeadWheel(LeftRear.deviceName, DcMotorEx.class, REVERSE),
	MiddleDeadWheel(LeftFront.deviceName,DcMotorEx.class, REVERSE),
	RightDeadWheel(RightFront.deviceName, DcMotorEx.class, REVERSE),
	imu(namespace.Imu, BNO055IMU .class);
	public final String deviceName;
	public final Class<?> classType;
	public final DeviceConfigPackage config;

	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType,DeviceConfigPackage config){
		this.config=config;
		this.classType=classType;
		this.deviceName=deviceName;
	}
	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType){
		this(deviceName,classType,new DeviceConfigPackage().AutoComplete());
	}
	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType,HardwareState state){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType, Direction direction){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction));
	}
	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType,HardwareState state, Direction direction){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDevices(String deviceName, Class<?> classType, Direction direction,HardwareState state){
		this(deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
}