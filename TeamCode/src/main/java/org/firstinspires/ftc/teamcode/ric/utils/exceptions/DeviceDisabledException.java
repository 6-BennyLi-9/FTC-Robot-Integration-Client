package org.firstinspires.ftc.teamcode.ric.utils.exceptions;

public class DeviceDisabledException extends NullPointerException{
	public DeviceDisabledException(String s){
		super("Device "+s+" is/are Disabled!");
	}
}
