package core.Utils.Exceptions;

public class DeviceDisabledException extends NullPointerException{
	public DeviceDisabledException(String s){
		super("Device "+s+" is/are Disabled!");
	}
}
