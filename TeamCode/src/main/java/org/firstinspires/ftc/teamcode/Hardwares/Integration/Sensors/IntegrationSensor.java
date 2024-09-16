package org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors;

public abstract class IntegrationSensor {
	public final String name;

	public IntegrationSensor(String name){
		this.name=name;
	}

	public abstract void update();
}
