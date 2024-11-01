package org.firstinspires.ftc.teamcode.utils.exceptions;

public class DeviceNotFoundException extends NullPointerException{
	public DeviceNotFoundException(final String s){
		super("Device "+s+" not found.");
	}
}
