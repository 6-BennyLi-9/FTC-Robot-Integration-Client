package org.firstinspires.ftc.teamcode.Utils;

public class Mathematics {
	/**
	 * @return 强制使value在(min,max)范围内，方法为“限制”
	 */
	public static double intervalClip(double value,double min,double max){
		return Math.min(Math.max(min,value),max);
	}
	/**
	 * @return 强制使value在(min,max)范围内，方法为“园滚”，遇到歧义自动取正
	 */
	public static double roundClip(double value,double max){
		double cache=value;
		while(cache>max)cache-=max*2;
		while(cache<=-max)cache+=max*2;
		return cache;
	}

	/**
	 * @param angle 像30,90,114.514的浮点数
	 * @return 合理化的angle,使其保证在[-180,180]的范围内，存在180与-180同义的问题（自动转为正数）
	 */
	public static double angleRationalize(double angle){
		return roundClip(angle,360);
	}

	/**
	 * @param radians 像PI,2*PI,0.16*PI的数
	 * @return 合理化的radians,使其保证在[-PI,PI]的范围内，存在PI与-PI同义的问题（自动转为正数）
	 */
	public static double radiansRationalize(double radians){
		return roundClip(radians,Math.PI);
	}
}
