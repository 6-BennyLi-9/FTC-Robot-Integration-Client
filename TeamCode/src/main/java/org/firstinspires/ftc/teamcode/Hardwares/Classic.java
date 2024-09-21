package org.firstinspires.ftc.teamcode.Hardwares;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyTag;
import org.firstinspires.ftc.teamcode.Utils.Enums.Quadrant;
import org.firstinspires.ftc.teamcode.Utils.Functions;

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

	public void drive(@NonNull DriveDirection driveDirection, double power) {
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
			motors.update(sensors.RobotAngle());
		}
	}

	/**
	 * @param angle 是较于x轴的度数
	 */
	public void drive(@NonNull DriveDirection driveDirection, @NonNull Quadrant quadrant, double power, double angle) {
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
			motors.update(sensors.RobotAngle());
		}
	}

	/**
	 * @param angle 相较于机器的正方向，允许为[-180,180]内的实数(不是弧度，不是弧度，不是弧度）
	 */
	public void SimpleDrive(double power,double angle){
		angle= Functions.angleRationalize(angle);

		if(angle==0){
			drive(DriveDirection.forward,power);
		}else if(angle==90){
			drive(DriveDirection.right,power);
		}else if(angle==-90){
			drive(DriveDirection.left,power);
		}else if(angle==180){
			drive(DriveDirection.back,power);
		}

		if(angle>0&&angle<90){//第一象限
			drive(DriveDirection.slant, Quadrant.firstQuadrant,power,90-angle);
		}else if(angle>90&&angle<180){//第四象限
			drive(DriveDirection.slant, Quadrant.forthQuadrant,power,angle-90);
		}else if(angle>-90&angle<0){//第二象限
			drive(DriveDirection.slant, Quadrant.secondQuadrant,power,90+angle);
		}else if(angle>-180&&angle<0){//第三象限
			drive(DriveDirection.slant, Quadrant.thirdQuadrant,power,-90-angle);
		}
	}

	public void SimpleRadiansDrive(double power,double radians){
		radians=Functions.radiansRationalize(radians);

		SimpleDrive(power,Math.toDegrees(radians));
	}

	/**
	 * 停止机器的移动程序，updateDriveOptions
	 */
	public void STOP(){
		motors.clearDriveOptions();
		sensors.update();
		motors.updateDriveOptions(sensors.RobotAngle());
	}
	public void operateThroughGamePad(@NonNull IntegrationGamepad gamepad){
		if(gamepad.map.containsKeySetting(KeyTag.ClassicSpeedControl)){
			BufPower+=gamepad.getRodState(KeyTag.ClassicSpeedControl)*0.6;
		}else if(gamepad.map.containsKeySetting(KeyTag.ClassicSpeedConfig)){
			if(gamepad.getButtonRunAble(KeyTag.ClassicSpeedConfig)){
				BufPower=0.9;
			}else{
				BufPower=0.3;
			}
		}
		BufPower=Functions.intervalClip(BufPower,-1,1);

		motors.simpleMotorPowerController(
				gamepad.getRodState(KeyTag.ClassicRunForward)*BufPower* Params.factorXPower,
				gamepad.getRodState(KeyTag.ClassicRunStrafe)*BufPower* Params.factorYPower,
				gamepad.getRodState(KeyTag.ClassicTurn)*BufPower* Params.factorHeadingPower
		);
	}
}