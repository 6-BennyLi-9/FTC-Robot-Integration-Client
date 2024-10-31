package org.firstinspires.ftc.teamcode.hardwares.controllers;

import static org.firstinspires.ftc.teamcode.Global.client;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.PositionalIntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceDisabledException;

/**
 * 集成化控制所有电机
 * <p>
 * 不会自动 update()
 *
 * @see Params.Configs
 * @see IntegrationMotor
 */
public class Motors {
	public IntegrationHardwareMap hardware;

	public IntegrationMotor LeftFront,LeftRear,RightFront,RightRear;

	//除非在手动程序中，不建议直接更改下列数值
	public double LeftFrontPower,RightFrontPower,LeftRearPower,RightRearPower;
	public double xAxisPower,yAxisPower,headingPower;
	public double SuspensionArmPower,IntakePower;

	private double ChassisBufPower =1, StructureBufPower =1;

	public Motors(IntegrationHardwareMap deviceMap){
		hardware= deviceMap;
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;

		SuspensionArmPower=0;
		IntakePower=0;

		LeftFront= (IntegrationMotor) hardware.getDevice(HardwareDeviceTypes.LeftFront);
		LeftRear = (IntegrationMotor) hardware.getDevice(HardwareDeviceTypes.LeftRear);
		RightFront= (IntegrationMotor) hardware.getDevice(HardwareDeviceTypes.RightFront);
		RightRear= (IntegrationMotor) hardware.getDevice(HardwareDeviceTypes.RightRear);
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
	 * @see Params
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
		Timer timer=new Timer();
		LeftFront.setPower( LeftFrontPower* ChassisBufPower);
		LeftRear.setPower( LeftRearPower* ChassisBufPower);
		RightFront.setPower( RightFrontPower* ChassisBufPower);
		RightRear.setPower( RightRearPower* ChassisBufPower);

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			return;
		}
		timer.restart();
		LeftFront.update();
		LeftRear.update();
		RightFront.update();
		RightRear.update();
		client.changeData("update time",timer.stopAndGetDeltaTime());
	}
	public void updateStructureOptions(){
		hardware.setPower(HardwareDeviceTypes.Intake, IntakePower * StructureBufPower);
		hardware.setPower(HardwareDeviceTypes.SuspensionArm, SuspensionArmPower* StructureBufPower);

		hardware.getDevice(HardwareDeviceTypes.Intake).update();
		hardware.getDevice(HardwareDeviceTypes.SuspensionArm).update();
	}

	public PositionalIntegrationMotor placementArm(){
		return (PositionalIntegrationMotor) hardware.getDevice(HardwareDeviceTypes.PlacementArm);
	}

	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see Params
	 */
	public void update(double headingDeg){
		if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower){
			update();
		}

		powersRationalize();

		updateDriveOptions(headingDeg);
		updateStructureOptions();

		if(Params.Configs.autoPrepareForNextOptionWhenUpdate){
			clearDriveOptions();
		}
	}
	public void update(){
		powersRationalize();

		updateDriveOptions();
		updateStructureOptions();

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
		LeftFrontPower  += yAxisPower-xPoser-headingPower;
		LeftRearPower   += yAxisPower+xPoser-headingPower;
		RightFrontPower += yAxisPower+xPoser+headingPower;
		RightRearPower  += yAxisPower-xPoser+headingPower;

		powersRationalize();
	}
	public void setChassisBufPower(double BufPower){
		ChassisBufPower =BufPower;
	}
	public void setStructureBufPower(double BufPower){
		StructureBufPower =BufPower;
	}
	public void setBufPower(double BudPower){
		setChassisBufPower(BudPower);
		setStructureBufPower(BudPower);
	}

	private void powersRationalize(){
		LeftFrontPower= Mathematics.intervalClip(LeftFrontPower,-1,1);
		LeftRearPower=Mathematics.intervalClip(LeftRearPower,-1,1);
		RightFrontPower=Mathematics.intervalClip(RightFrontPower,-1,1);
		RightRearPower=Mathematics.intervalClip(RightRearPower,-1,1);

		SuspensionArmPower=Mathematics.intervalClip(SuspensionArmPower,-1,1);
		IntakePower=Mathematics.intervalClip(IntakePower,-1,1);
	}

	public void showPowers(){
		client.changeData("LeftFrontPower",LeftFrontPower);
		client.changeData("LeftRearPower",LeftRearPower);
		client.changeData("RightFrontPower",RightFrontPower);
		client.changeData("RightRearPower",RightRearPower);

		client.changeData("SuspensionArmPower",SuspensionArmPower);
		client.changeData("IntakePower",IntakePower);

		try{client.changeData("LeftFrontPower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.LeftFront)).getPower());}catch (
				DeviceDisabledException ignored){}
		try{client.changeData("LeftRearPower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.LeftRear)).getPower());}catch (DeviceDisabledException ignored){}
		try{client.changeData("RightFrontPower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.RightFront)).getPower());}catch (DeviceDisabledException ignored){}
		try{client.changeData("RightRearPower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.RightRear)).getPower());}catch (DeviceDisabledException ignored){}

		try{client.changeData("SuspensionArmPower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.SuspensionArm)).getPower());}catch (DeviceDisabledException ignored){}
		try{client.changeData("IntakePower(integration)",((IntegrationMotor)hardware.getDevice(HardwareDeviceTypes.Intake)).getPower());}catch (DeviceDisabledException ignored){}
	}
}
