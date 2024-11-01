package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace;

public class DeviceConfigPackage {
	public HardwareState state;
	public enum Direction{
		Forward,
		Reversed
	}
	public Direction direction;
	public boolean isDeadWheel;

	public DeviceConfigPackage(){
		this.state =HardwareState.Enabled;
		this.direction =Direction.Forward;
	}
	public DeviceConfigPackage AutoComplete(){
		if(null == state) this.state =HardwareState.Enabled;
		if(null == direction) this.direction = Direction.Forward;
		return this;
	}
	public DeviceConfigPackage AddConfig(final HardwareState state){
		this.state=state;
		return this;
	}
	public DeviceConfigPackage AddConfig(final Direction direction){
		this.direction=direction;
		return this;
	}
}
