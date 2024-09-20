package org.firstinspires.ftc.teamcode.Hardwares.Namespace;

import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;

public class DeviceConfigPackage {
	public HardwareState state;
	public enum Direction{
		Forward,
		Reversed
	}
	public Direction direction;
	public DeviceConfigPackage(){
		state=null;
		direction=null;
	}
	public DeviceConfigPackage AutoComplete(){
		if(state==null)state=HardwareState.Enabled;
		if(direction==null)direction= Direction.Forward;
		return this;
	}
	public DeviceConfigPackage AddConfig(HardwareState state){
		this.state=state;
		return this;
	}
	public DeviceConfigPackage AddConfig(Direction direction){
		this.direction=direction;
		return this;
	}
}
