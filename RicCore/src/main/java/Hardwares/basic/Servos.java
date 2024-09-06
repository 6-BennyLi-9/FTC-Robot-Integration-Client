package Hardwares.basic;

import Hardwares.namespace.DeviceMap;
import Hardwares.namespace.HardwareDevices;

public class Servos {
	public DeviceMap hardware;
	public double FrontClipPosition,RearClipPosition;

	private final static double AllowErrorPosition=0.1;
	private boolean PositionInPlace;

	public Servos(DeviceMap hardware){
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
	public boolean InPlace(){
		return PositionInPlace;
	}
}
