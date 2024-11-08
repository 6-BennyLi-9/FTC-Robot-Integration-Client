package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace;

import static org.firstinspires.ftc.teamcode.Params.HardwareNamespace.*;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.TeamTag.utilTeamTag;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DeviceDirection.NotAvailable;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DeviceDirection.Reversed;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareState.Disabled;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.TeamTag;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 可以自动登记硬件的名字及其类型
 * @see Params.HardwareNamespace
 */
public enum HardwareDeviceTypes {
	LeftFront       (utilTeamTag, leftFront,                DcMotorEx.class,    Reversed),
	RightFront      (utilTeamTag, rightFront,               DcMotorEx.class),
	LeftRear        (utilTeamTag, leftRear,                 DcMotorEx.class,    Reversed),
	RightRear       (utilTeamTag, rightRear,                DcMotorEx.class),
	PlacementArm    (utilTeamTag, placementArm,             DcMotorEx.class,    Disabled),
	Intake          (utilTeamTag, intake,                   DcMotorEx.class,    Disabled),
	FrontClip       (utilTeamTag, frontClip,                Servo.class,        Disabled),
	RearClip        (utilTeamTag, rearClip,                 Servo.class,        Disabled),
	SuspensionArm   (utilTeamTag, suspensionArm,            DcMotorEx.class,    Disabled),
	LeftDeadWheel   (utilTeamTag, LeftRear.deviceName,      DcMotorEx.class,    Reversed),
	MiddleDeadWheel (utilTeamTag, LeftFront.deviceName,     DcMotorEx.class,    Reversed),
	RightDeadWheel  (utilTeamTag, RightFront.deviceName,    DcMotorEx.class,    Reversed),
	imu             (utilTeamTag, Params.HardwareNamespace.imu, BNO055IMU.class, NotAvailable);
	public final String deviceName;
	public final Class<?> classType;
	public final DeviceConfigPackage config;
	public final TeamTag teamCode;
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType, final DeviceConfigPackage config){
		this.config=config;
		this.classType=classType;
		this.deviceName=deviceName;
		this.teamCode=teamCode;
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AutoComplete());
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType, final HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType, final DeviceDirection direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType, final HardwareState state, final DeviceDirection direction){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
	@UserRequirementFunctions
	HardwareDeviceTypes(final TeamTag teamCode, final String deviceName, final Class<?> classType, final DeviceDirection direction, final HardwareState state){
		this(teamCode, deviceName,classType,new DeviceConfigPackage().AddConfig(direction).AddConfig(state));
	}
}