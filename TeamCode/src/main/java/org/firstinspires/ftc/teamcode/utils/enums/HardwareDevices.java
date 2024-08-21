package org.firstinspires.ftc.teamcode.utils.enums;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * 仅适用于类型为<code>HardwareDevice</code> 接口下的硬件
 * <p>
 * 可以自动登记硬件的名字及其类型
 */
public enum HardwareDevices {
	LeftFront("leftFront", DcMotorEx.class),
	RightFront("eightFront", DcMotorEx.class),
	LeftRear("leftRear", DcMotorEx.class),
	RightRear("rightRear", DcMotorEx.class),
	PlacementArm("rightLift", DcMotorEx.class),
	Intake("intake", DcMotorEx.class),
	FrontClip("frontClip", Servo.class),
	RearClip("rearClip", Servo.class),
	SuspensionArm("rack", DcMotorEx.class);
	public final String deviceName;
	public final Class<?> classType;

	HardwareDevices(String deviceName, Class<?> classType){
		this.deviceName=deviceName;
		this.classType=classType;
	}
}