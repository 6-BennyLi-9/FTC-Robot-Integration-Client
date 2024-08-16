package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.namespace;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.enums.Hardware;

public class Motors {
	public HardwareSet hardware;

	//除非在手动程序中，不建议直接更改下列数值
	public double LeftFrontPower,RightFrontPower,LeftRearPower,RightRearPower;
	public double xAxisPower,yAxisPower,headingPower;
	public double PlacementArmPower,SuspensionArmPower,IntakePower;

	private double ClassicBufPower =1, StructureBufPower =1;

	public Motors(@NonNull HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace = new namespace();
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;

		PlacementArmPower=0;
		SuspensionArmPower=0;
		IntakePower=0;

		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.LeftFront)),Hardware.LeftFront);
		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.LeftRear)),Hardware.LeftRear);
		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.RightFront)),Hardware.RightFront);
		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.RightRear)),Hardware.RightRear);

		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.PlacementArm)),Hardware.PlacementArm);
		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.SuspensionArm)),Hardware.SuspensionArm);
		hardware.addDevice(hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(Hardware.Intake)),Hardware.Intake);

		//TODO:根据实际情况修改
		hardware.setDirection(Hardware.LeftFront, DcMotorSimple.Direction.FORWARD);
		hardware.setDirection(Hardware.LeftRear, DcMotorSimple.Direction.FORWARD);
		hardware.setDirection(Hardware.RightFront, DcMotorSimple.Direction.REVERSE);
		hardware.setDirection(Hardware.RightRear, DcMotorSimple.Direction.REVERSE);

		hardware.setDirection(Hardware.PlacementArm, DcMotorSimple.Direction.REVERSE);
		hardware.setDirection(Hardware.Intake, DcMotorSimple.Direction.REVERSE);
		hardware.setDirection(Hardware.SuspensionArm, DcMotorSimple.Direction.FORWARD);
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
			double currentXPower,currentYPower,currentHeadingPower=headingPower;
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
			
			simpleMotorPowerController(currentXPower,currentYPower,currentHeadingPower);
		}
		updateDriveOptions();
	}
	public void updateDriveOptions(){
		LeftFrontPower=Mathematics.intervalClip(LeftFrontPower,-1,1);
		LeftRearPower=Mathematics.intervalClip(LeftRearPower,-1,1);
		RightFrontPower=Mathematics.intervalClip(RightFrontPower,-1,1);
		RightRearPower=Mathematics.intervalClip(RightRearPower,-1,1);
//		hardware.setPower(
		hardware.setPower(Hardware.LeftFront, LeftFrontPower* ClassicBufPower);
		hardware.setPower(Hardware.LeftRear, LeftRearPower* ClassicBufPower);
		hardware.setPower(Hardware.RightFront, RightFrontPower* ClassicBufPower);
		hardware.setPower(Hardware.RightRear, RightRearPower* ClassicBufPower);
	}
	public void updateStructureOptions(){
		hardware.setPower(Hardware.PlacementArm, PlacementArmPower* StructureBufPower);
		hardware.setPower(Hardware.Intake, IntakePower* StructureBufPower);
		hardware.setPower(Hardware.SuspensionArm, SuspensionArmPower* StructureBufPower);
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
	public void update(){
		updateDriveOptions();
		updateStructureOptions();
		if(RuntimeOption.autoPrepareForNextOptionWhenUpdate){
			clearDriveOptions();
		}
	}
	
	/**
	 * @param xPoser 机器平移力
	 * @param yAxisPower 机器前行/后退力
	 * @param headingPower 机器旋转力
	 */
	public void simpleMotorPowerController(double xPoser,double yAxisPower,double headingPower){
		LeftFrontPower  += yAxisPower+xPoser-headingPower;
		LeftRearPower   += yAxisPower-xPoser-headingPower;
		RightFrontPower += yAxisPower-xPoser+headingPower;
		RightRearPower  += yAxisPower+xPoser+headingPower;
	}
	public void setClassicBufPower(double BufPower){
		ClassicBufPower =BufPower;
	}
	public void setStructureBufPower(double BufPower){
		StructureBufPower =BufPower;
	}
	public void setBufPower(double BudPower){
		setClassicBufPower(BudPower);
		setStructureBufPower(BudPower);
	}
}
