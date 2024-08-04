package org.firstinspires.ftc.teamcode.Hardwares;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.enums.place;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;

public class Classic {
	protected Motors motors;

	public Classic(Motors motors) {
		this.motors = motors;
	}

	public void drive(@NonNull driveDirection driveDirection, double power) {
		switch ( driveDirection ) {
			case forward:
				motors.LeftFrontPower+=power;
				motors.LeftRearPower+=power;
				motors.RightFrontPower+=power;
				motors.RightRearPower+=power;
				break;
			case back:
				motors.LeftFrontPower -= power;
				motors.LeftRearPower -= power;
				motors.RightFrontPower -= power;
				motors.RightRearPower -= power;
				break;
			case left:
				motors.LeftFrontPower -= power;
				motors.LeftRearPower+=power;
				motors.RightFrontPower+=power;
				motors.RightRearPower -= power;
				break;
			case right:
				motors.LeftFrontPower+=power;
				motors.LeftRearPower -= power;
				motors.RightFrontPower -= power;
				motors.RightRearPower+=power;
				break;
			case turn:
				motors.LeftFrontPower+=power;
				motors.LeftRearPower+=power;
				motors.RightFrontPower -= power;
				motors.RightRearPower -= power;
				break;
			case slant:
				Log.e("UnExpectingCode","ErrorCode#1");
		}

		if( RuntimeOption.runUpdateWhenAnyNewOptionsAdded ){
			motors.update();
		}
	}

	/**
	 * @param angle 是较于x轴的度数
	 */
	public void drive(@NonNull driveDirection driveDirection, @NonNull place place, double power, double angle) {
		switch ( driveDirection ) {
			case forward:case back:case left:case right:
			case turn:
				drive(driveDirection, power);
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
				motors.LeftFrontPower+=y + x;
				motors.LeftRearPower+=y - x;
				motors.RightFrontPower+=y - x;
				motors.RightRearPower+=y + x;
				break;
		}

		if( RuntimeOption.runUpdateWhenAnyNewOptionsAdded ){
			motors.update();
		}
	}

	/**
	 * @param angle 相较于机器的正方向，允许为[-180,180]内的实数(不是弧度，不是弧度，不是弧度）
	 */
	public void SimpleDrive(double power,double angle){
		angle= Mathematics.angleRationalize(angle);

		if(angle==0){
			drive(driveDirection.forward,power);
		}else if(angle==90){
			drive(driveDirection.right,power);
		}else if(angle==-90){
			drive(driveDirection.left,power);
		}else if(angle==180){
			drive(driveDirection.back,power);
		}

		if(angle>0&&angle<90){//第一象限
			drive(driveDirection.slant,place.A,power,90-angle);
		}else if(angle>90&&angle<180){//第四象限
			drive(driveDirection.slant,place.D,power,angle-90);
		}else if(angle>-90&angle<0){//第二象限
			drive(driveDirection.slant,place.B,power,90+angle);
		}else if(angle>-180&&angle<0){//第三象限
			drive(driveDirection.slant,place.C,power,-90-angle);
		}
	}

	public void SimpleRadiansDrive(double power,double radians){
		radians=Mathematics.radiansRationalize(radians);

		if(radians==0){
			drive(driveDirection.forward,power);
		}else if(radians==Math.PI/2){
			drive(driveDirection.right,power);
		}else if(radians==-Math.PI/2){
			drive(driveDirection.left,power);
		}else if(radians==Math.PI){
			drive(driveDirection.back,power);
		}

		if(radians>0&&radians<Math.PI/2){//第一象限
			drive(driveDirection.slant,place.A,power,Math.toRadians(Math.PI/2-radians));
		}else if(radians>Math.PI/2&&radians<Math.PI){//第四象限
			drive(driveDirection.slant,place.D,power,Math.toRadians(radians-Math.PI/2));
		}else if(radians>-Math.PI/2&radians<0){//第二象限
			drive(driveDirection.slant,place.B,power,Math.toRadians(Math.PI/2+radians));
		}else if(radians>-Math.PI&&radians<0){//第三象限
			drive(driveDirection.slant,place.C,power,Math.toRadians(-Math.PI/2-radians));
		}
	}
}