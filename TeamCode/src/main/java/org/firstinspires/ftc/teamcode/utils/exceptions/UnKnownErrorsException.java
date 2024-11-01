package org.firstinspires.ftc.teamcode.utils.exceptions;

public class UnKnownErrorsException extends RuntimeException{
	public UnKnownErrorsException(final String s){
		super("UnKnown Type"+s);
	}
}
