package org.firstinspires.ftc.teamcode.ric.utils.exceptions;

public class DeviceNotFoundException extends NullPointerException{
	public DeviceNotFoundException(String s){
		super("Device "+s+" not found.");
	}
}
