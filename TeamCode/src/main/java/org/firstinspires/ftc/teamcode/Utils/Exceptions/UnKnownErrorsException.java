package org.firstinspires.ftc.teamcode.Utils.Exceptions;

public class UnKnownErrorsException extends RuntimeException{
	public UnKnownErrorsException(String s){
		super("UnKnown Type"+s);
	}
}
