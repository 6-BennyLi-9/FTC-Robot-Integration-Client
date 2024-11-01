package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.Quadrant;

/**
 * 但凡Java支持重载运算符，都不至于写得这么复杂
 */
public class Complex {
	public static class imaginaryNumber {
		public double factor;
		imaginaryNumber(final double factor){
			this.factor=factor;
		}
	}
	public imaginaryNumber ImaginaryPart;
	public double RealPart;
	
	
	public Complex(@NonNull final Vector2d position){
		this(position.x,position.y);
	}
	public Complex(final double degree){
		this(Math.cos(Mathematics.angleRationalize(degree)),Math.sin(Mathematics.angleRationalize(degree)));
	}
	public Complex(final double RealPart, final double ImaginaryPartFactor){
		this(RealPart,new imaginaryNumber(ImaginaryPartFactor));
	}
	Complex(final double RealPart, final imaginaryNumber ImaginaryPart){
		this.RealPart=RealPart;
		this.ImaginaryPart= ImaginaryPart;
	}

	public double imaginary(){
		return this.ImaginaryPart.factor;
	}
	public double magnitude(){
		return Math.sqrt(this.RealPart * this.RealPart + this.imaginary() * this.imaginary());
	}
	@UtilFunctions
	public Complex plus(@NonNull final Complex val){
		return new Complex(this.RealPart + val.RealPart, this.imaginary() + val.imaginary());
	}
	@UtilFunctions
	public Complex negative(){
		return new Complex(- this.RealPart,- this.imaginary());
	}
	@UtilFunctions
	public Complex minus(@NonNull final Complex val){
		return this.plus(val.negative());
	}
	@UtilFunctions
	public Complex times(@NonNull final Complex val){
		return new Complex(this.RealPart * val.RealPart - this.imaginary() * val.imaginary(), this.RealPart * val.imaginary() + this.imaginary() * this.RealPart
		);
	}
	@UtilFunctions
	public Complex times(final double val){
		return new Complex(this.RealPart * val, this.imaginary() * val
		);
	}

	/**
	 *  (a+bi)/(c+di)
	 * =(ac+bd)/(cc+dd)+(bc-ad)/(cc+dd)i
	 */
	@UtilFunctions
	public Complex divide(@NonNull final Complex val){
		return new Complex(
				(this.RealPart * val.RealPart + this.imaginary() * val.imaginary())
				/(val.RealPart* val.RealPart+val.imaginary()*val.imaginary()),
				(this.imaginary() * val.RealPart - this.RealPart * val.imaginary())
				/(val.RealPart* val.RealPart+val.imaginary()*val.imaginary())
		);
	}
	@UtilFunctions
	public Complex divide(final double val){
		return this.divide(new Complex(val,0));
	}

	public Vector2d toVector2d(){
		return new Vector2d(this.RealPart, this.imaginary());
	}
	
	/**
	 * @return 返回该复数的幅角，范围在[-PI,PI]，如果复数的与原点重合，会抛出错误
	 */
	public double arg(){
		if(0 < RealPart){
			return Math.atan(this.imaginary() / this.RealPart);
		}else if(0 < imaginary() && 0 == RealPart){
			return Math.PI/2;
		}else if(0 > imaginary() && 0 == RealPart){
			return -Math.PI/2;
		}else if(0 > RealPart && 0 <= imaginary()){
			return Math.atan(this.imaginary() / this.RealPart) + Math.PI;
		}else if(0 > RealPart && 0 > imaginary()){
			return Math.atan(this.imaginary() / this.RealPart - Math.PI);
		}else{
			return 0;
		}
	}

	/**
	 * @return 返回该复数的幅角，范围在[-180,180]，如果复数的与原点重合，会抛出错误
	 */
	public double toDegree(){
		return Math.toDegrees(this.arg());
	}
	public double angleToYAxis(){
		return Math.abs(this.toDegree() - 90);
	}
	public Quadrant quadrant(){
		if(0 < RealPart && 0 <= imaginary()){
			return Quadrant.firstQuadrant;
		}else if(0 >= RealPart && 0 < imaginary()){
			return Quadrant.secondQuadrant;
		}else if(0 > RealPart && 0 >= imaginary()){
			return Quadrant.thirdQuadrant;
		} else if (0 <= RealPart && 0 > imaginary()) {
			return Quadrant.forthQuadrant;
		}else{
			throw new RuntimeException("Unexpected Value:The Complex can't be 0");
		}
	}

	@NonNull
	@Override
	public String toString() {
		return this.RealPart + "+" + this.imaginary() + "i";
	}
}
