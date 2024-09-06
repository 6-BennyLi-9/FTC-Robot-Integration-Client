package core.Hardwares;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import Hardwares.basic.Motors;
import Hardwares.basic.Sensors;
import core.Params;
import core.Utils.Enums.Quadrant;
import core.Utils.Enums.driveDirection;
import core.Utils.Mathematics;

public class Classic {
	public Motors motors;
	public Sensors sensors;
	
	/**
	 * 该BufPower只用于手动程序中
	 */
	private double BufPower=1;

	public Classic(Motors motors,Sensors sensors) {
		this.motors     =motors;
		this.sensors    =sensors;
	}

	public void drive(@NonNull driveDirection driveDirection, double power) {
		switch ( driveDirection ) {
			case forward:
				motors.yAxisPower+=power;
				break;
			case back:
				motors.yAxisPower-=power;
				break;
			case left:
				motors.xAxisPower-=power;
				break;
			case right:
				motors.xAxisPower+=power;
				break;
			case turn:
				motors.headingPower+=power;
				break;
			case slant:
				Log.e("UnExpectingCode","ErrorCode#1");
		}

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			sensors.update();
			motors.update(sensors.FirstAngle);
		}
	}

	/**
	 * @param angle 是较于x轴的度数
	 */
	public void drive(@NonNull driveDirection driveDirection, @NonNull Quadrant quadrant, double power, double angle) {
		switch ( driveDirection ) {
			case forward:case back:case left:case right:
			case turn:
				drive(driveDirection, power);
				break;
			case slant:
				double x=0,y=0;
				switch (quadrant) {
					case firstQuadrant:
						x = Math.cos(angle) * power;
						y = Math.sin(angle) * power;
						break;
					case secondQuadrant:
						x = -Math.cos(angle) * power;
						y = Math.sin(angle) * power;
						break;
					case thirdQuadrant:
						x = -Math.cos(angle) * power;
						y = -Math.sin(angle) * power;
						break;
					case forthQuadrant:
						x = Math.cos(angle) * power;
						y = -Math.sin(angle) * power;
						break;
				}
				motors.xAxisPower+=x;
				motors.yAxisPower+=y;
				break;
		}

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			sensors.update();
			motors.update(sensors.FirstAngle);
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
			drive(driveDirection.slant, Quadrant.firstQuadrant,power,90-angle);
		}else if(angle>90&&angle<180){//第四象限
			drive(driveDirection.slant, Quadrant.forthQuadrant,power,angle-90);
		}else if(angle>-90&angle<0){//第二象限
			drive(driveDirection.slant, Quadrant.secondQuadrant,power,90+angle);
		}else if(angle>-180&&angle<0){//第三象限
			drive(driveDirection.slant, Quadrant.thirdQuadrant,power,-90-angle);
		}
	}

	public void SimpleRadiansDrive(double power,double radians){
		radians=Mathematics.radiansRationalize(radians);

		SimpleDrive(power,Math.toDegrees(radians));
	}

	/**
	 * 停止机器的移动程序，updateDriveOptions
	 */
	public void STOP(){
		motors.clearDriveOptions();
		sensors.update();
		motors.updateDriveOptions(sensors.FirstAngle);
	}
	public void operateThroughGamePad(@NonNull Gamepad gamepad){
		if(Params.Configs.useRightStickYToConfigRobotSpeed){
			BufPower+=gamepad.right_stick_y*0.6;
			BufPower=Mathematics.intervalClip(BufPower,-1,1);
			motors.simpleMotorPowerController(
					gamepad.left_stick_x*BufPower* Params.factorXPower,
					gamepad.left_stick_y*BufPower* Params.factorYPower,
					gamepad.right_stick_x*BufPower* Params.factorHeadingPower
			        );
		}else {
			motors.simpleMotorPowerController(
					gamepad.left_stick_x* Params.factorXPower,
					gamepad.left_stick_y* Params.factorYPower,
					gamepad.right_stick_x* Params.factorHeadingPower
			        );
		}
	}
}