package org.firstinspires.ftc.teamcode.Utils;

import org.firstinspires.ftc.teamcode.Utils.Enums.DeviceDirection;
import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;

public class DeviceConfigPackage {
	public HardwareState state;
	public DeviceDirection direction;
	public DeviceConfigPackage(){
		state=null;
		direction=null;
	}
	public DeviceConfigPackage AutoComplete(){
		if(state==null)state=HardwareState.Enabled;
		if(direction==null)direction=DeviceDirection.FORWARD;
		return this;
	}
	public DeviceConfigPackage AddConfig(HardwareState state){
		this.state=state;
		return this;
	}
	public DeviceConfigPackage AddConfig(DeviceDirection direction){
		this.direction=direction;
		return this;
	}
}
