package org.firstinspires.ftc.teamcode.hardwares;

import static org.firstinspires.ftc.teamcode.Params.Configs;
import static org.firstinspires.ftc.teamcode.Params.factorHeadingPower;
import static org.firstinspires.ftc.teamcode.Params.factorXPower;
import static org.firstinspires.ftc.teamcode.Params.factorYPower;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.controllers.Motors;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Sensors;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.enums.Quadrant;

/**
 * 集成的底盘操控程序
 *
 * @see Motors
 * @see Sensors
 */
public class Chassis {
	public Motors motors;
	public Sensors sensors;
	
	/**
	 * 该BufPower只用于手动程序中
	 */
	private double BufPower=1;

	public Chassis(final Motors motors, final Sensors sensors) {
		this.motors     =motors;
		this.sensors    =sensors;
	}

	public void drive(@NonNull final DriveDirection driveDirection, final double power) {
		switch ( driveDirection ) {
			case forward:
				this.motors.yAxisPower+=power;
				break;
			case back:
				this.motors.yAxisPower-=power;
				break;
			case left:
				this.motors.xAxisPower-=power;
				break;
			case right:
				this.motors.xAxisPower+=power;
				break;
			case turn:
				this.motors.headingPower+=power;
				break;
			case slant:
				Log.e("UnExpectingCode","ErrorCode#1");
		}

		if( Configs.runUpdateWhenAnyNewOptionsAdded ){
			this.sensors.updateBNO();
			this.motors.update(this.sensors.robotAngle());
		}
	}

	/**
	 * @param angle 是较于x轴的度数
	 */
	public void drive(@NonNull final DriveDirection driveDirection, @NonNull final Quadrant quadrant, final double power, final double angle) {
		switch ( driveDirection ) {
			case forward:case back:case left:case right:
			case turn:
				this.drive(driveDirection, power);
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
				this.motors.xAxisPower+=x;
				this.motors.yAxisPower+=y;
				break;
		}

		if( Configs.runUpdateWhenAnyNewOptionsAdded ){
			this.sensors.updateBNO();
			this.motors.update(this.sensors.robotAngle());
		}
	}

	/**
	 * @param angle 相较于机器的正方向，允许为[-180,180]内的实数(不是弧度，不是弧度，不是弧度）
	 */
	public void SimpleDrive(final double power, double angle){
		angle= Mathematics.angleRationalize(angle);

		if(0 == angle){
			this.drive(DriveDirection.forward,power);
		}else if(90 == angle){
			this.drive(DriveDirection.right,power);
		}else if(- 90 == angle){
			this.drive(DriveDirection.left,power);
		}else if(180 == angle){
			this.drive(DriveDirection.back,power);
		}

		if(0 < angle && 90 > angle){//第一象限
			this.drive(DriveDirection.slant, Quadrant.firstQuadrant,power, 90 - angle);
		}else if(90 < angle && 180 > angle){//第四象限
			this.drive(DriveDirection.slant, Quadrant.forthQuadrant,power, angle - 90);
		}else if(- 90 < angle & 0 > angle){//第二象限
			this.drive(DriveDirection.slant, Quadrant.secondQuadrant,power, 90 + angle);
		}else if(- 180 < angle && 0 > angle){//第三象限
			this.drive(DriveDirection.slant, Quadrant.thirdQuadrant,power, -90 - angle);
		}
	}

	public void SimpleRadiansDrive(final double power, double radians){
		radians= Mathematics.radiansRationalize(radians);

		this.SimpleDrive(power,Math.toDegrees(radians));
	}
	public void configureBufPower(@NonNull final IntegrationGamepad gamepad){
		if(gamepad.keyMap.containsKeySetting(KeyTag.ChassisSpeedControl)){
			this.BufPower += gamepad.getRodState(KeyTag.ChassisSpeedControl) * 0.6;
		}else if(gamepad.keyMap.containsKeySetting(KeyTag.ChassisSpeedConfig)){
			if(gamepad.getButtonRunAble(KeyTag.ChassisSpeedConfig)){
				this.BufPower =0.9;
			}else{
				this.BufPower =0.3;
			}
		}
		this.BufPower = Mathematics.intervalClip(this.BufPower, - 1, 1);
	}
	public void driveFromGamepad(@NonNull final IntegrationGamepad gamepad){
		this.motors.simpleMotorPowerController(
				gamepad.getRodState(KeyTag.ChassisRunForward) * this.BufPower * factorXPower,
				gamepad.getRodState(KeyTag.ChassisRunStrafe) * this.BufPower * factorYPower,
				gamepad.getRodState(KeyTag.ChassisTurn) * this.BufPower * factorHeadingPower
		);
	}

	/**
	 * 停止机器的移动程序，updateDriveOptions
	 */
	public void STOP(){
		this.motors.clearDriveOptions();
		this.sensors.updateBNO();
		this.motors.updateDriveOptions(this.sensors.robotAngle());
	}
	public void operateThroughGamePad(@NonNull final IntegrationGamepad gamepad){
		this.configureBufPower(gamepad);

		this.driveFromGamepad(gamepad);
	}
}