package org.firstinspires.ftc.teamcode.ric.hardwares.basic;

import static org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes.FrontClip;
import static org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes.RearClip;

import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationHardwareMap;

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
			hardware.setPosition(FrontClip,FrontClipPosition);
			hardware.setPosition(RearClip,RearClipPosition);

			PositionInPlace=hardware.isInPlace(FrontClip)&&hardware.isInPlace(RearClip);
		}catch (Exception ignored){}
	}

	/**
	 * @return 是否所有舵机都到位了
	 */
	public boolean inPlace(){
		return PositionInPlace;
	}
}
