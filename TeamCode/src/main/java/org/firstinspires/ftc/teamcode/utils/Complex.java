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
		this(Math.cos(Mathematics.angleRationalize(degree)),Math.sin(Mathematics.angleRationalize(degree)));
	}
	public Complex(double RealPart,double ImaginaryPartFactor){
		this(RealPart,new imaginaryNumber(ImaginaryPartFactor));
	}
	Complex(double RealPart,imaginaryNumber ImaginaryPart){
		this.RealPart=RealPart;
		this.ImaginaryPart= ImaginaryPart;
	}

	public double imaginary(){
		return ImaginaryPart.factor;
	}
	public double magnitude(){
		return Math.sqrt(RealPart*RealPart + imaginary()*imaginary());
	}
	@UtilFunctions
	public Complex plus(@NonNull Complex val){
		return new Complex(RealPart+ val.RealPart,imaginary()+val.imaginary());
	}
	@UtilFunctions
	public Complex negative(){
		return new Complex(-RealPart,-imaginary());
	}
	@UtilFunctions
	public Complex minus(@NonNull Complex val){
		return plus(val.negative());
	}
	@UtilFunctions
	public Complex times(@NonNull Complex val){
		return new Complex(
				RealPart* val.RealPart-imaginary()*val.imaginary(),
				RealPart*val.imaginary()+imaginary()*RealPart
		);
	}
	@UtilFunctions
	public Complex times(double val){
		return new Complex(
				RealPart*val,
				imaginary()*val
		);
	}

	/**
	 *  (a+bi)/(c+di)
	 * =(ac+bd)/(cc+dd)+(bc-ad)/(cc+dd)i
	 */
	@UtilFunctions
	public Complex divide(@NonNull Complex val){
		return new Complex(
				(RealPart*val.RealPart+imaginary()*val.imaginary())
				/(val.RealPart* val.RealPart+val.imaginary()*val.imaginary()),
				(imaginary()*val.RealPart-RealPart*val.imaginary())
				/(val.RealPart* val.RealPart+val.imaginary()*val.imaginary())
		);
	}
	@UtilFunctions
	public Complex divide(double val){
		return divide(new Complex(val,0));
	}

	public Vector2d toVector2d(){
		return new Vector2d(RealPart,imaginary());
	}
	
	/**
	 * @return 返回该复数的幅角，范围在[-PI,PI]，如果复数的与原点重合，会抛出错误
	 */
	public double arg(){
		if(RealPart>0){
			return Math.atan(imaginary()/RealPart);
		}else if(imaginary()>0&&RealPart==0){
			return Math.PI/2;
		}else if(imaginary()<0&&RealPart==0){
			return -Math.PI/2;
		}else if(RealPart<0&&imaginary()>=0){
			return Math.atan(imaginary()/RealPart)+Math.PI;
		}else if(RealPart<0&&imaginary()<0){
			return Math.atan(imaginary()/RealPart-Math.PI);
		}else{
			throw new RuntimeException("Unexpected Value:The Complex can't be 0");
		}
	}

	/**
	 * @return 返回该复数的幅角，范围在[-180,180]，如果复数的与原点重合，会抛出错误
	 */
	public double toDegree(){
		return Math.toDegrees(arg());
	}
	public double angleToYAxis(){
		return Math.abs(toDegree()-90);
	}
	public Quadrant quadrant(){
		if(RealPart>0&&imaginary()>=0){
			return Quadrant.firstQuadrant;
		}else if(RealPart<=0&&imaginary()>0){
			return Quadrant.secondQuadrant;
		}else if(RealPart<0&&imaginary()<=0){
			return Quadrant.thirdQuadrant;
		} else if (RealPart >= 0 && imaginary() < 0) {
			return Quadrant.forthQuadrant;
		}else{
			throw new RuntimeException("Unexpected Value:The Complex can't be 0");
		}
	}

	@NonNull
	@Override
	public String toString() {
		return RealPart+"+"+imaginary()+"i";
	}
}
