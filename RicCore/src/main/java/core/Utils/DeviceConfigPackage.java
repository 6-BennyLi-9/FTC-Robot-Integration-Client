package core.Utils;

import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

import core.Utils.Enums.HardwareState;


public class DeviceConfigPackage {
	public HardwareState state;
	public Direction direction;
	public DeviceConfigPackage(){
		state=null;
		direction=null;
	}
	public DeviceConfigPackage AutoComplete(){
		if(state==null)state=HardwareState.Enabled;
		if(direction==null)direction= Direction.FORWARD;
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
