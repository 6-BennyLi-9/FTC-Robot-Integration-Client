package core.Hardwares.basic;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import core.robotcore.external.navigation.AngleUnit;
import core.robotcore.external.navigation.AxesOrder;
import core.robotcore.external.navigation.AxesReference;
import Hardwares.namespace.DeviceMap;
import Hardwares.namespace.HardwareDevices;
import core.Params;
import core.Utils.Enums.DeadWheelsType;

public class Sensors {
	/** BNO055IMU 比 IMU 的稳定性更好
	 */
	public BNO055IMU imu;
	//TODO:按需求修改
	public final DeadWheelsType DeadWheelType =DeadWheelsType.ThreeDeadWheels;
	public Encoder Left,Middle,Right;
	public double LeftTick,MiddleTick,RightTick;
	public double LastLeftTick,LastMiddleTick,LastRightTick;
	public double FirstAngle,XMoved,YMoved,LastXMoved, LastYMoved,LastFirstAngle;

	public Sensors(@NonNull DeviceMap deviceMap){
		imu= (BNO055IMU) deviceMap.getDevice(HardwareDevices.imu);

		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit= BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit= BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile="BNO055Calibration.json";
		parameters.loggingEnabled=true;
		parameters.loggingTag="IMU";
		parameters.accelerationIntegrationAlgorithm=new JustLoggingAccelerationIntegrator();
		imu.initialize(parameters);
		Left    =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.LeftDeadWheel)));
		Middle  =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.MiddleDeadWheel)));
		Right   =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.RightDeadWheel)));

		LastYMoved=0;
		LastXMoved=0;
		LastFirstAngle=0;
		LeftTick=0;
		MiddleTick=0;
		RightTick=0;
		LastLeftTick=0;
		LastMiddleTick=0;
		LastRightTick=0;
	}

	public void update(){
		LastXMoved=XMoved;
		LastYMoved=YMoved;
		LastFirstAngle=FirstAngle;

		FirstAngle=imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
		XMoved=imu.getGravity().xAccel;
		YMoved=imu.getGravity().yAccel;

		LastLeftTick=LeftTick;
		LastMiddleTick=MiddleTick;
		LastRightTick=RightTick;

		PositionVelocityPair left,middle,right;
		switch (DeadWheelType) {
			case BE_NOT_USING_DEAD_WHEELS:
				break;
			case TwoDeadWheels:
				left=Left.getPositionAndVelocity();
				right=Right.getPositionAndVelocity();
				LeftTick=left.position;
				RightTick=right.position;
				break;
			case ThreeDeadWheels:
				left=Left.getPositionAndVelocity();
				middle=Middle.getPositionAndVelocity();
				right=Right.getPositionAndVelocity();
				LeftTick=left.position;
				MiddleTick=middle.position;
				RightTick=right.position;
				break;
		}
	}

	/**
	 * @return 机器前进的TICK数
	 */
	public double getDeltaA(){
		return (LeftTick-LastLeftTick+RightTick-LastRightTick)/2;
	}
	/**
	 * @return 机器平移的TICK数
	 */
	public double getDeltaL(){
		return MiddleTick-LastMiddleTick-getDeltaA()*Params.AxialPosition/Params.LateralPosition*4;
	}
	/**
	 * @return 机器旋转的TICK数
	 */
	public double getDeltaT(){
		return (LeftTick-LastLeftTick-RightTick+LastRightTick)/2;
	}
}
