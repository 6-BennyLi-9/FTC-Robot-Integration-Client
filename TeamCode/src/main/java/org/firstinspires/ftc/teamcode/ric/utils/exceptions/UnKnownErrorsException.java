package org.firstinspires.ftc.teamcode.ric.utils.exceptions;

public class UnKnownErrorsException extends RuntimeException{
	public UnKnownErrorsException(String s){
		super("UnKnown Type"+s);
	}
}
