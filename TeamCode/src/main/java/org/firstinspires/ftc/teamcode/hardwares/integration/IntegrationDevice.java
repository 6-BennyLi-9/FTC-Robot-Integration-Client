package org.firstinspires.ftc.teamcode.hardwares.integration;

public abstract class IntegrationDevice implements Integrations{
	public final String name;
	protected String pidTag=null;
	public boolean updated=false;

	public IntegrationDevice(String name){
		this.name=name;
	}

	public abstract void update();
	public double getPower(){return 0;}
	public abstract double getPosition();
}