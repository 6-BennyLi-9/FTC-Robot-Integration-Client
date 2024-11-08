package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace;

import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

public class DeviceConfigNode {
	public final HardwareState state;
	public final DeviceDirection direction;

	@UserRequirementFunctions
	public DeviceConfigNode(){
		this.state =HardwareState.Enabled;
		this.direction = DeviceDirection.Forward;
	}
	@UserRequirementFunctions
	public DeviceConfigNode(final HardwareState state){
		this.state =state;
		this.direction = DeviceDirection.Forward;
	}
	@UserRequirementFunctions
	public DeviceConfigNode(final DeviceDirection direction){
		this.state =HardwareState.Enabled;
		this.direction = direction;
	}
	@UserRequirementFunctions
	public DeviceConfigNode(final HardwareState state,final DeviceDirection direction){
		this.state =state;
		this.direction = direction;
	}
}
