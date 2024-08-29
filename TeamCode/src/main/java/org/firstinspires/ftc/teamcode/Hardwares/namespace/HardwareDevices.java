package org.firstinspires.ftc.teamcode.Hardwares.namespace;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params.namespace;
import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;

/**
 * 可以自动登记硬件的名字及其类型
 * @see namespace
 */
public enum HardwareDevices {
	LeftFront(namespace.LeftFront, DcMotorEx.class,HardwareState.Enabled),
	RightFront(namespace.RightFront, DcMotorEx.class,HardwareState.Enabled),
	LeftRear(namespace.LeftRear, DcMotorEx.class,HardwareState.Enabled),
	RightRear(namespace.RightRear, DcMotorEx.class,HardwareState.Enabled),
	PlacementArm(namespace.PlacementArm, DcMotorEx.class,HardwareState.Disabled),
	Intake(namespace.Intake, DcMotorEx.class,HardwareState.Disabled),
	FrontClip(namespace.FrontClip, Servo.class,HardwareState.Disabled),
	RearClip(namespace.RearClip, Servo.class,HardwareState.Disabled),
	SuspensionArm(namespace.SuspensionArm, DcMotorEx.class,HardwareState.Disabled),
	LeftDeadWheel(LeftRear.deviceName, DcMotorEx.class,HardwareState.Enabled),
	MiddleDeadWheel(LeftFront.deviceName,DcMotorEx.class,HardwareState.Enabled),
	RightDeadWheel(RightFront.deviceName, DcMotorEx.class,HardwareState.Enabled),
	imu(namespace.Imu, BNO055IMU .class,HardwareState.Enabled);
	public final String deviceName;
	public final Class<?> classType;
	public final HardwareState state;

	HardwareDevices(String deviceName, Class<?> classType,HardwareState state){
		this.deviceName=deviceName;
		this.classType=classType;
		this.state=state;
	}
}