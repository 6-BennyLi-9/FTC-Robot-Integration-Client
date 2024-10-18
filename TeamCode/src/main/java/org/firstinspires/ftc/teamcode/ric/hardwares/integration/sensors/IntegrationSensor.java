package org.firstinspires.ftc.teamcode.ric.hardwares.integration.sensors;

import org.firstinspires.ftc.teamcode.ric.hardwares.integration.Integrations;

public abstract class IntegrationSensor implements Integrations {
	public final String name;

	public IntegrationSensor(String name){
		this.name=name;
	}

	public abstract void update();
}
