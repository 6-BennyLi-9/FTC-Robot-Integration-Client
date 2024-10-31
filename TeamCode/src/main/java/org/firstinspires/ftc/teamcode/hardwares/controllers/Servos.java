package org.firstinspires.ftc.teamcode.hardwares.controllers;

import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareDeviceTypes;

/**
 * 集成化控制所有舵机
 * <p>
 * 不会自动 update()
 *
 * @see IntegrationServo
 */
public class Servos {
	public IntegrationHardwareMap hardware;
	public double FrontClipPosition,RearClipPosition;
	private boolean PositionInPlace;

	public Servos(IntegrationHardwareMap hardware){
		this.hardware=hardware;
		PositionInPlace=false;
	}

	public void update(){
		try {
			hardware.setPosition(HardwareDeviceTypes.FrontClip,FrontClipPosition);
			hardware.setPosition(HardwareDeviceTypes.RearClip,RearClipPosition);

			PositionInPlace=hardware.isInPlace(HardwareDeviceTypes.FrontClip)&&hardware.isInPlace(HardwareDeviceTypes.RearClip);
		}catch (Exception ignored){}
	}

	/**
	 * @return 是否所有舵机都到位了
	 */
	public boolean inPlace(){
		return PositionInPlace;
	}
}
