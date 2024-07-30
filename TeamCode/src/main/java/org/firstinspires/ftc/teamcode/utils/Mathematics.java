package org.firstinspires.ftc.teamcode.utils;

public class Mathematics {
	public static double intervalClip(double value,double min,double max){
		return Math.min(Math.max(min,value),max);
	}
}
