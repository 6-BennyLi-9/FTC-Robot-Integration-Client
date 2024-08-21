package org.firstinspires.ftc.teamcode.utils.enums;

/**
 * 仅适用于类型不是<code>HardwareDevice</code> 接口下的硬件
 */
public enum RobotDevices {
	imu("imu");
	public final String name;
	RobotDevices(String name){
		this.name=name;
	}
}
