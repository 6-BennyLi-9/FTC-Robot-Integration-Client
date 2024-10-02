package org.firstinspires.ftc.teamcode.Hardwares.Basic;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Complex;
import org.firstinspires.ftc.teamcode.Utils.Functions;
import org.firstinspires.ftc.teamcode.Utils.Mathematics;

public class Motors {
	public IntegrationHardwareMap hardware;

	//除非在手动程序中，不建议直接更改下列数值
	public double LeftFrontPower,RightFrontPower,LeftRearPower,RightRearPower;
	public double xAxisPower,yAxisPower,headingPower;
	public double PlacementArmPower,SuspensionArmPower,IntakePower;

	private double ClassicBufPower =1, StructureBufPower =1;

	public Motors(IntegrationHardwareMap deviceMap){
		hardware= deviceMap;
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;

		PlacementArmPower=0;
		SuspensionArmPower=0;
		IntakePower=0;

		//TODO:根据实际情况修改
		hardware.setDirection(HardwareDeviceTypes.LeftFront, DcMotorSimple.Direction.FORWARD);
		hardware.setDirection(HardwareDeviceTypes.LeftRear, DcMotorSimple.Direction.FORWARD);
		hardware.setDirection(HardwareDeviceTypes.RightFront, DcMotorSimple.Direction.REVERSE);
		hardware.setDirection(HardwareDeviceTypes.RightRear, DcMotorSimple.Direction.REVERSE);

		try {
			hardware.setDirection(HardwareDeviceTypes.PlacementArm, DcMotorSimple.Direction.REVERSE);
			hardware.setDirection(HardwareDeviceTypes.Intake, DcMotorSimple.Direction.REVERSE);
			hardware.setDirection(HardwareDeviceTypes.SuspensionArm, DcMotorSimple.Direction.FORWARD);
		}catch (Exception ignored){}
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
	 * @see org.firstinspires.ftc.teamcode.Params
	 */
	public void updateDriveOptions(double headingDeg){
		if( Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower ){
			double currentXPower,currentYPower,currentHeadingPower=headingPower;
			headingDeg= Functions.angleRationalize(headingDeg);//防止有问题
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

		hardware.setPowerSmooth(HardwareDeviceTypes.LeftFront, LeftFrontPower* ClassicBufPower);
		hardware.setPowerSmooth(HardwareDeviceTypes.LeftRear, LeftRearPower* ClassicBufPower);
		hardware.setPowerSmooth(HardwareDeviceTypes.RightFront, RightFrontPower* ClassicBufPower);
		hardware.setPowerSmooth(HardwareDeviceTypes.RightRear, RightRearPower* ClassicBufPower);
	}
	public void updateStructureOptions(){
		hardware.setPower(HardwareDeviceTypes.PlacementArm, PlacementArmPower* StructureBufPower);
		hardware.setPower(HardwareDeviceTypes.Intake, IntakePower* StructureBufPower);
		hardware.setPower(HardwareDeviceTypes.SuspensionArm, SuspensionArmPower* StructureBufPower);
	}
	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see org.firstinspires.ftc.teamcode.Params
	 */
	public void update(double headingDeg){
		updateDriveOptions(headingDeg);
		try{
			updateStructureOptions();
		}catch (Exception ignored){}

		if(Params.Configs.autoPrepareForNextOptionWhenUpdate){
			clearDriveOptions();
		}
	}
	public void update(){
		updateDriveOptions();
		try{
			updateStructureOptions();
		}catch (Exception ignored){}

		if(Params.Configs.autoPrepareForNextOptionWhenUpdate){
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
