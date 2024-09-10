package org.firstinspires.ftc.teamcode.Utils;

public class Mathematics {
	public static double intervalClip(double value,double min,double max){
		return Math.min(Math.max(min,value),max);
	}

	/**
	 * @param angle 像30,90,114.514的浮点数
	 * @return 合理化的angle,使其保证在[-180,180]的范围内，存在180与-180同义的问题（自动转为正数）
	 */
	public static double angleRationalize(double angle){
		double cache=angle;
		while(cache>180)cache-=360;
		while(cache<=-180)cache+=360;
		return cache;
	}

	/**
	 * @param radians 像PI,2*PI,0.16*PI的数
	 * @return 合理化的radians,使其保证在[-PI,PI]的范围内，存在PI与-PI同义的问题（自动转为正数）
	 */
	public static double radiansRationalize(double radians){
		return Math.toRadians(angleRationalize(Math.toDegrees(radians)));
	}
}
