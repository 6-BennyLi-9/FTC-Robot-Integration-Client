package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import org.firstinspires.ftc.teamcode.hardwares.integration.Integrations;

public abstract class IntegrationSensor implements Integrations {
	public final String name;

	protected IntegrationSensor(final String name){
		this.name=name;
	}

	public abstract void update();
}
