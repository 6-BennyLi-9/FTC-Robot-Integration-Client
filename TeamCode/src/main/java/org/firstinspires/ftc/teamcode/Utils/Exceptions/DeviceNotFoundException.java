package org.firstinspires.ftc.teamcode.Utils.Exceptions;

public class DeviceNotFoundException extends NullPointerException{
	public DeviceNotFoundException(String s){
		super("Device "+s+" not found.");
	}
}
