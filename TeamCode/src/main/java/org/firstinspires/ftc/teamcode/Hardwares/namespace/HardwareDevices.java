package org.firstinspires.ftc.teamcode.Hardwares.namespace;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.Enums.HardwareState;

//TODO:以下名字均为我们团队所测定的
/**
 * 仅适用于类型为<code>HardwareDevice</code> 接口下的硬件
 * <p>
 * 可以自动登记硬件的名字及其类型
 */
public enum HardwareDevices {
	LeftFront("leftFront", DcMotorEx.class,HardwareState.Enabled),
	RightFront("rightFront", DcMotorEx.class,HardwareState.Enabled),
	LeftRear("leftBack", DcMotorEx.class,HardwareState.Enabled),
	RightRear("rightBack", DcMotorEx.class,HardwareState.Enabled),
	PlacementArm("rightLift", DcMotorEx.class,HardwareState.Disabled),
	Intake("intake", DcMotorEx.class,HardwareState.Disabled),
	FrontClip("frontClip", Servo.class,HardwareState.Disabled),
	RearClip("rearClip", Servo.class,HardwareState.Disabled),
	SuspensionArm("rack", DcMotorEx.class,HardwareState.Disabled),
	LeftDeadWheel(LeftRear.deviceName, DcMotorEx.class,HardwareState.Enabled),
	MiddleDeadWheel(LeftFront.deviceName,DcMotorEx.class,HardwareState.Enabled),
	RightDeadWheel(RightFront.deviceName, DcMotorEx.class,HardwareState.Enabled);
	public final String deviceName;
	public final Class<?> classType;
	public final HardwareState state;

	HardwareDevices(String deviceName, Class<?> classType,HardwareState state){
		this.deviceName=deviceName;
		this.classType=classType;
		this.state=state;
	}
}