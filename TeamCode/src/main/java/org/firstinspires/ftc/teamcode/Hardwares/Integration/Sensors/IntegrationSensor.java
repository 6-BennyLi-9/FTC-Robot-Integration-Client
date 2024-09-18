package org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Integrations;

public abstract class IntegrationSensor implements Integrations {
	public final String name;

	public IntegrationSensor(String name){
		this.name=name;
	}

	public abstract void update();
}
