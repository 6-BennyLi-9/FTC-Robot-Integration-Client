package org.firstinspires.ftc.teamcode.Hardwares.Basic;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;

public class Servos {
	public IntegrationHardwareMap hardware;
	public double FrontClipPosition,RearClipPosition;

	private final static double AllowErrorPosition=0.1;
	private boolean PositionInPlace;

	public Servos(IntegrationHardwareMap hardware){
		this.hardware=hardware;
		PositionInPlace=false;
	}

	public void update(){
		try {
			hardware.setPosition(HardwareDevices.FrontClip,FrontClipPosition);
			hardware.setPosition(HardwareDevices.RearClip,RearClipPosition);

			PositionInPlace=(Math.abs(hardware.getPosition(HardwareDevices.RearClip) - RearClipPosition)  < AllowErrorPosition) &&
					(Math.abs(hardware.getPosition(HardwareDevices.FrontClip)- FrontClipPosition) < AllowErrorPosition);
		}catch (Exception ignored){}
	}

	/**
	 * @return 是否所有舵机都到位了
	 */
	public boolean inPlace(){
		return PositionInPlace;
	}
}
