package org.firstinspires.ftc.teamcode.Hardwares.namespace;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

//TODO:以下名字均为我们团队所测定的
/**
 * 仅适用于类型为<code>HardwareDevice</code> 接口下的硬件
 * <p>
 * 可以自动登记硬件的名字及其类型
 */
public enum HardwareDevices {
	LeftFront("leftFront", DcMotorEx.class),
	RightFront("rightFront", DcMotorEx.class),
	LeftRear("leftRear", DcMotorEx.class),
	RightRear("rightRear", DcMotorEx.class),
	PlacementArm("rightLift", DcMotorEx.class),
	Intake("intake", DcMotorEx.class),
	FrontClip("frontClip", Servo.class),
	RearClip("rearClip", Servo.class),
	SuspensionArm("rack", DcMotorEx.class),
	LeftDeadWheel("leftFront",DcMotorEx.class),
	MiddleDeadWheel("leftRear",DcMotorEx.class),
	RightDeadWheel("rightFront",DcMotorEx.class);
	public final String deviceName;
	public final Class<?> classType;

	HardwareDevices(String deviceName, Class<?> classType){
		this.deviceName=deviceName;
		this.classType=classType;
	}
}