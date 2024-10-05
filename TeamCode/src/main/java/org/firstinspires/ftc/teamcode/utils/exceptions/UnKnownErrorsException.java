package org.firstinspires.ftc.teamcode.utils.exceptions;

public class UnKnownErrorsException extends RuntimeException{
	public UnKnownErrorsException(String s){
		super("UnKnown Type"+s);
	}
}
