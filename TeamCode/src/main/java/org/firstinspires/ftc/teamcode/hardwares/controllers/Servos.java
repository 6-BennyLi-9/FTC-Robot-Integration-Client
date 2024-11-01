package org.firstinspires.ftc.teamcode.hardwares.controllers;

import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;

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

	public Servos(final IntegrationHardwareMap hardware){
		this.hardware=hardware;
		this.PositionInPlace =false;
	}

	public void update(){
		try {
			this.hardware.setPosition(HardwareDeviceTypes.FrontClip, this.FrontClipPosition);
			this.hardware.setPosition(HardwareDeviceTypes.RearClip, this.RearClipPosition);

			this.PositionInPlace = this.hardware.isInPlace(HardwareDeviceTypes.FrontClip) && this.hardware.isInPlace(HardwareDeviceTypes.RearClip);
		}catch (final Exception ignored){}
	}

	/**
	 * @return 是否所有舵机都到位了
	 */
	public boolean inPlace(){
		return this.PositionInPlace;
	}
}
