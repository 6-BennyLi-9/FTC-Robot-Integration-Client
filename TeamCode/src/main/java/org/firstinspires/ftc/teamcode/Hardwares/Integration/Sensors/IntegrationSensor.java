package org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors;

import org.firstinspires.ftc.teamcode.Utils.Timer;

public abstract class IntegrationSensor {
	public final String name;
	protected final Timer timer;

	public IntegrationSensor(String name){
		this.name=name;
		timer=new Timer();
	}

	public abstract void update();
}
