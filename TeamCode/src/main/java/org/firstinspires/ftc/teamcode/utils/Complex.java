package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

/**
 * 但凡Java支持重载运算符，都不至于写得这么复杂
 */
public class Complex {
	public static class imaginaryNumber {
		public double factor;
		imaginaryNumber(double factor){
			this.factor=factor;
		}
	}
	public imaginaryNumber ImaginaryPart;
	public double RealPart;
	public Complex(@NonNull Vector2d position){
		this(position.x,position.y);
	}
	public Complex(double degree){
		this(Math.cos(degree),Math.sin(degree));
	}
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
	public Complex minus(@NonNull Complex val){
		return plus(val.negative());
	}
	public Complex times(@NonNull Complex val){
		return new Complex(
				this.RealPart* val.RealPart-this.ImaginaryPart.factor*val.ImaginaryPart.factor,
				this.RealPart*val.ImaginaryPart.factor+this.ImaginaryPart.factor*this.RealPart
		);
	}
	public Complex times(double val){
		return new Complex(
				this.RealPart*val,
				this.ImaginaryPart.factor*val
		);
	}

	/**
	 * (a+bi)/(c+di)
	 * (ac+bd)/(cc+dd)+(bc-ad)/(cc+dd)i
	 */
	public Complex divide(@NonNull Complex val){
		return new Complex(
				(this.RealPart*val.RealPart+this.ImaginaryPart.factor*val.ImaginaryPart.factor)
				/(val.RealPart* val.RealPart+val.ImaginaryPart.factor*val.ImaginaryPart.factor),
				(this.ImaginaryPart.factor*val.RealPart-this.RealPart*val.ImaginaryPart.factor)
				/(val.RealPart* val.RealPart+val.ImaginaryPart.factor*val.ImaginaryPart.factor)
		);
	}
	public Complex divide(double val){
		return new Complex(this.RealPart/val,this.ImaginaryPart.factor/val);
	}

	public Vector2d toVector2d(){
		return new Vector2d(this.RealPart,this.ImaginaryPart.factor);
	}
	public double toDegree(){
		return Math.atan2(this.ImaginaryPart.factor,this.RealPart);
	}
}
