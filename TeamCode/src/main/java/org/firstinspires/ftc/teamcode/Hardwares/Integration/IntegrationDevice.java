package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import org.firstinspires.ftc.teamcode.Utils.Timer;

public abstract class IntegrationDevice {
	public final String name;
	protected String pidTag=null;
	protected final Timer timer;
	public boolean updated=false;

	public IntegrationDevice(String name){
		this.name=name;
		timer=new Timer();
	}

	public abstract void update();
	protected double getPower(){return 0;}
	protected abstract double getPosition();
}
