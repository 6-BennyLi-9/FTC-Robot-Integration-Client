package org.firstinspires.ftc.teamcode.Hardwares;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;

public class Classic {
	public enum simpleDirection {
		back,
		forward,
		left,
		right,
		slant,
		turn
	}

	public enum place {
		/**
		 * <p>A:第一象限</p>
		 * <p>B:第二象限</p>
		 * <p>C:第三象限</p>
		 * <p>D:第四象限</p>
		 */
		A,
		B,
		C,
		D
	}

	protected Motors motors;

	public Classic(Motors motors) {
		this.motors = motors;
	}

	public void drive(@NonNull simpleDirection simpleDirection, double power) {
		switch (simpleDirection) {
			case forward:
				motors.LeftFront.setPower(power);
				motors.LeftRear.setPower(power);
				motors.RightFront.setPower(power);
				motors.RightRear.setPower(power);
				break;
			case back:
				motors.LeftFront.setPower(-power);
				motors.LeftRear.setPower(-power);
				motors.RightFront.setPower(-power);
				motors.RightRear.setPower(-power);
				break;
			case left:
				motors.LeftFront.setPower(-power);
				motors.LeftRear.setPower(power);
				motors.RightFront.setPower(power);
				motors.RightRear.setPower(-power);
				break;
			case right:
				motors.LeftFront.setPower(power);
				motors.LeftRear.setPower(-power);
				motors.RightFront.setPower(-power);
				motors.RightRear.setPower(power);
				break;
			case turn:
				motors.LeftFront.setPower(power);
				motors.LeftRear.setPower(power);
				motors.RightFront.setPower(-power);
				motors.RightRear.setPower(-power);
				break;
			case slant:
				Log.e("UnExpectingCode","ErrorCode#1");
		}
	}

	/**
	 * @param angle 是较于x轴的度数
	 */
	public void drive(@NonNull simpleDirection simpleDirection,@NonNull place place, double power, double angle) {
		switch (simpleDirection) {
			case forward:case back:case left:case right:
			case turn:
				drive(simpleDirection, power);
				break;
			case slant:
				double x=0,y=0;
				switch (place) {
					case A:
						x = Math.cos(angle) * power;
						y = Math.sin(angle) * power;
						break;
					case B:
						x = -Math.cos(angle) * power;
						y = Math.sin(angle) * power;
						break;
					case C:
						x = -Math.cos(angle) * power;
						y = -Math.sin(angle) * power;
						break;
					case D:
						x = Math.cos(angle) * power;
						y = -Math.sin(angle) * power;
						break;
				}
				motors.LeftFront.setPower(y + x);
				motors.LeftRear.setPower(y - x);
				motors.RightFront.setPower(y - x);
				motors.RightRear.setPower(y + x);
				break;
		}
	}

	/**
	 * @param angle 相较于机器的正方向，允许为[-180,180]内的实数
	 */
	public void SimpleDrive(double power,double angle){
		while (angle<=-180)angle+=360;
		while (angle>180)angle-=360;

		if(angle==0){
			drive(simpleDirection.forward,power);
		}else if(angle==90){
			drive(simpleDirection.right,power);
		}else if(angle==-90){
			drive(simpleDirection.left,power);
		}else if(angle==180){
			drive(simpleDirection.back,power);
		}

		if(angle>0&&angle<90){//第一象限
			drive(simpleDirection.slant,place.A,power,90-angle);
		}else if(angle>90&&angle<180){//第四象限
			drive(simpleDirection.slant,place.D,power,angle-90);
		}else if(angle>-90&angle<0){//第二象限
			drive(simpleDirection.slant,place.B,power,90+angle);
		}else if(angle>-180&&angle<0){//第三象限
			drive(simpleDirection.slant,place.C,power,-90-angle);
		}
	}
}