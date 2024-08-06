package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

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

	public double magnitude(){
		return Math.sqrt(RealPart*RealPart + ImaginaryPart.factor*ImaginaryPart.factor);
	}
	public Complex plus(@NonNull Complex val){
		return new Complex(this.RealPart+ val.RealPart,this.ImaginaryPart.factor+val.ImaginaryPart.factor);
	}
	public Complex negative(){
		return new Complex(-this.RealPart,-this.ImaginaryPart.factor);
	}
	public Complex minus(Complex val){
		return plus(val.negative());
	}
	public Complex times(Complex val){
		return new Complex(
				this.RealPart* val.RealPart-this.ImaginaryPart.factor*val.ImaginaryPart.factor,
				this.RealPart*val.ImaginaryPart.factor+this.ImaginaryPart.factor*this.RealPart
		);
	}

	/**
	 * (a+bi)/(c+di)
	 * (ac+bd)/(cc+dd)+(bc-ad)/(cc+dd)i
	 */
	public Complex divide(Complex val){
		return new Complex(
				(this.RealPart*val.RealPart+this.ImaginaryPart.factor*val.ImaginaryPart.factor)
				/(val.RealPart* val.RealPart+val.ImaginaryPart.factor*val.ImaginaryPart.factor),
				(this.ImaginaryPart.factor*val.RealPart-this.RealPart*val.ImaginaryPart.factor)
				/(val.RealPart* val.RealPart+val.ImaginaryPart.factor*val.ImaginaryPart.factor)
		);
	}
}
