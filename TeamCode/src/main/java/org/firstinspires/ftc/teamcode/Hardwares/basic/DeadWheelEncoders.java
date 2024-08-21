package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares.namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.utils.enums.DeadWheelsType;

public class DeadWheelEncoders {
	public static class Params{
		/**
		 * 左侧死轮和右侧死轮的距离
		 */
		public static double LateralPosition=0;
		/**
		 * 机器中心到中间死轮(前端死轮)的距离
		 */
		public static double AxialPosition=0;
	}

	//TODO:按需求修改
	public final DeadWheelsType type=DeadWheelsType.BE_NOT_USING_DEAD_WHEELS;
	public Encoder Left,Middle,Right;
	public double AxialTicks,TurningTicks,LateralTicks;

	public final double vI;

	public DeadWheelEncoders(@NonNull DeviceMap deviceMap){
		Left    =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.LeftDeadWheel)));
		Middle  =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.MiddleDeadWheel)));
		Right   =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.RightDeadWheel)));
		vI=Params.LateralPosition/Params.AxialPosition/2;
	}

	public void update(){
		PositionVelocityPair left,middle,right;
		switch (type) {
			case BE_NOT_USING_DEAD_WHEELS:
				break;
			case TwoDeadWheels:
				left=Left.getPositionAndVelocity();
				right=Right.getPositionAndVelocity();

				AxialTicks=(left.position+right.position)/2.0;
				TurningTicks=(right.position-left.position)/2.0;
				break;
			case ThreeDeadWheels:
				left=Left.getPositionAndVelocity();
				middle=Middle.getPositionAndVelocity();
				right=Right.getPositionAndVelocity();

				AxialTicks=(left.position+right.position)/2.0;
				TurningTicks=(right.position-left.position)/2.0;
				LateralTicks=middle.position-TurningTicks*vI;
				break;
		}
	}
}
