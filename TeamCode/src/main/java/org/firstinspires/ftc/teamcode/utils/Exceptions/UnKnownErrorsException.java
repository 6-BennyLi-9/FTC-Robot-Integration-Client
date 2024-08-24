package org.firstinspires.ftc.teamcode.utils.Exceptions;

public class UnKnownErrorsException extends RuntimeException{
	public UnKnownErrorsException(String s){
		super("UnKnown Enum Type"+s);
	}
}
