package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.namespace;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.enums.hardware;

public class Motors {
	public DcMotorEx LeftFront,RightFront,LeftRear,RightRear;
	//除非在手动程序中，不建议直接更改下列数值
	public double LeftFrontPower,RightFrontPower,LeftRearPower,RightRearPower;
	public double xAxisPower,yAxisPower,headingPower;

	public DcMotorEx PlacementArm,SuspensionArm,Intake;
	public double PlacementArmPower,SuspensionArmPower,IntakePower;

	public Motors(@NonNull HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace = new namespace();
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;

		PlacementArmPower=0;
		SuspensionArmPower=0;
		IntakePower=0;

		LeftFront=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.LeftFront));
		LeftRear=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.LeftRear));
		RightFront=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.RightFront));
		RightRear=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.RightRear));

		PlacementArm=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.PlacementArm));
		SuspensionArm=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.SuspensionArm));
		Intake=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.Intake));

		//TODO:根据实际情况修改
		LeftFront.setDirection(DcMotorEx.Direction.FORWARD);
		LeftRear.setDirection(DcMotorEx.Direction.FORWARD);
		RightFront.setDirection(DcMotorEx.Direction.REVERSE);
		RightRear.setDirection(DcMotorEx.Direction.REVERSE);

		PlacementArm.setDirection(DcMotorEx.Direction.REVERSE);
		Intake.setDirection(DcMotorEx.Direction.REVERSE);
		SuspensionArm.setDirection(DcMotorEx.Direction.FORWARD);
	}

	public void clearDriveOptions(){
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;
		
		xAxisPower=0;
		yAxisPower=0;
	}

	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see org.firstinspires.ftc.teamcode.RuntimeOption
	 */
	public void updateDriveOptions(double headingDeg){
		if( RuntimeOption.driverUsingAxisPowerInsteadOfCurrentPower ){
			double currentXPower=0,currentYPower=0,currentHeadingPower=headingPower;
			headingDeg= Mathematics.angleRationalize(headingDeg);//防止有问题
			Complex aim=new Complex(new Vector2d(xAxisPower,yAxisPower)),robotHeading=new Complex(headingDeg);
			Complex Counterclockwise=new Complex(robotHeading.angleToYAxis());
			
			switch (robotHeading.quadrant()){
				case firstQuadrant://逆时针转
				case thirdQuadrant:
					aim=aim.times(Counterclockwise);
					break;
				case secondQuadrant://顺时针转
				case forthQuadrant:
					aim=aim.divide(Counterclockwise);
					break;
			}
			currentYPower=aim.imaginary();
			currentXPower=aim.RealPart;
			
			LeftFrontPower  += currentYPower+currentXPower-currentHeadingPower;
			LeftRearPower   += currentYPower-currentXPower-currentHeadingPower;
			RightFrontPower += currentYPower-currentXPower+currentHeadingPower;
			RightRearPower  += currentYPower+currentXPower+currentHeadingPower;
		}else {
			LeftFront.setPower(LeftFrontPower);
			LeftRear.setPower(LeftRearPower);
			RightFront.setPower(RightFrontPower);
			RightRear.setPower(RightRearPower);
		}
	}
	public void updateStructureOptions(){
		PlacementArm.setPower(PlacementArmPower);
		Intake.setPower(IntakePower);
		SuspensionArm.setPower(SuspensionArmPower);
	}
	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see org.firstinspires.ftc.teamcode.RuntimeOption
	 */
	public void update(double headingDeg){
		updateDriveOptions(headingDeg);
		updateStructureOptions();
		if(RuntimeOption.autoPrepareForNextOptionWhenUpdate){
			clearDriveOptions();
		}
	}
}
