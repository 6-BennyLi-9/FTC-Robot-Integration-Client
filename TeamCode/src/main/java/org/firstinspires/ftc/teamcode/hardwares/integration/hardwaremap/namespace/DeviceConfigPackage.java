package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace;

public class DeviceConfigPackage {
	public HardwareState state;
	public DeviceDirection direction;

	public DeviceConfigPackage(){
		this.state =HardwareState.Enabled;
		this.direction = DeviceDirection.Forward;
	}
	public DeviceConfigPackage AutoComplete(){
		if(null == state) this.state =HardwareState.Enabled;
		if(null == direction) this.direction = DeviceDirection.Forward;
		return this;
	}
	public DeviceConfigPackage AddConfig(final HardwareState state){
		this.state=state;
		return this;
	}
	public DeviceConfigPackage AddConfig(final DeviceDirection direction){
		this.direction=direction;
		return this;
	}
}
