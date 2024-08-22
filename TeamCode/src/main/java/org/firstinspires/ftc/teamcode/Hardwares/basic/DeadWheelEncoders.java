package org.firstinspires.ftc.teamcode.Hardwares.basic;

import static org.firstinspires.ftc.teamcode.Params.*;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares.namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.utils.enums.DeadWheelsType;

public class DeadWheelEncoders {
	//TODO:按需求修改
	public final DeadWheelsType type=DeadWheelsType.BE_NOT_USING_DEAD_WHEELS;
	protected Encoder Left,Middle,Right;
	public double AxialTicks,TurningTicks,LateralTicks;
	public double LastAxialTicks,LastTurningTicks,LastLateralTicks;
	private final double vI;

	public DeadWheelEncoders(@NonNull DeviceMap deviceMap){
		LastAxialTicks=0;
		LastLateralTicks=0;
		LastTurningTicks=0;

		Left    =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.LeftDeadWheel)));
		Middle  =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.MiddleDeadWheel)));
		Right   =new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(HardwareDevices.RightDeadWheel)));
		vI= LateralPosition/ AxialPosition/2;
	}

	public void update(){
		LastLateralTicks=LateralTicks;
		LastAxialTicks=AxialTicks;
		LastTurningTicks=TurningTicks;

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
