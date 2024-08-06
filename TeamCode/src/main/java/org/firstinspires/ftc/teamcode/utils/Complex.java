package org.firstinspires.ftc.teamcode.utils;

/**
 * 但凡Java支持重载运算符，都不至于写得这么复杂
 */
public class Complex {
	public static class imaginaryNumber {
		public double factor;
		imaginaryNumber(){
			this(1);
		}
		imaginaryNumber(double factor){
			this.factor=factor;
		}
	}
	public imaginaryNumber ImaginaryPart;
	public double RealPart;
	Complex(double RealPart,double ImaginaryPartFactor){
		this(RealPart,new imaginaryNumber(ImaginaryPartFactor));
	}
	Complex(double RealPart,imaginaryNumber ImaginaryPart){
		this.RealPart=RealPart;
		this.ImaginaryPart= ImaginaryPart;
	}
}
