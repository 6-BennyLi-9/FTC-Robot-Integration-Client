package org.firstinspires.ftc.teamcode.hardwares.controllers;

import static org.firstinspires.ftc.teamcode.Global.client;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.PositionalIntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
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

	public Motors(final IntegrationHardwareMap deviceMap){
		this.hardware = deviceMap;
		this.LeftFrontPower =0;
		this.RightFrontPower =0;
		this.LeftRearPower =0;
		this.RightRearPower =0;

		this.SuspensionArmPower =0;
		this.IntakePower =0;

		this.LeftFront = (IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.LeftFront);
		this.LeftRear = (IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.LeftRear);
		this.RightFront = (IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.RightFront);
		this.RightRear = (IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.RightRear);
	}

	public void clearDriveOptions(){
		this.LeftFrontPower =0;
		this.RightFrontPower =0;
		this.LeftRearPower =0;
		this.RightRearPower =0;

		this.xAxisPower =0;
		this.yAxisPower =0;
	}

	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see Params
	 */
	public void updateDriveOptions(double headingDeg){
		if( Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower ){
			final double currentXPower;
			final double       currentYPower;
			final double currentHeadingPower = this.headingPower;
			headingDeg= Mathematics.angleRationalize(headingDeg);//防止有问题
			Complex       aim              =new Complex(new Vector2d(this.xAxisPower, this.yAxisPower));
			final Complex robotHeading     =new Complex(headingDeg);
			final Complex Counterclockwise =new Complex(robotHeading.angleToYAxis());
			
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

			this.simpleMotorPowerController(currentXPower,currentYPower,currentHeadingPower);
		}
		this.updateDriveOptions();
	}
	public void updateDriveOptions(){
		final Timer timer =new Timer();
		this.LeftFront.setPower(this.LeftFrontPower * this.ChassisBufPower);
		this.LeftRear.setPower(this.LeftRearPower * this.ChassisBufPower);
		this.RightFront.setPower(this.RightFrontPower * this.ChassisBufPower);
		this.RightRear.setPower(this.RightRearPower * this.ChassisBufPower);

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			return;
		}
		timer.restart();
		this.LeftFront.update();
		this.LeftRear.update();
		this.RightFront.update();
		this.RightRear.update();
		client.changeData("update time",timer.stopAndGetDeltaTime());
	}
	public void updateStructureOptions(){
		this.hardware.setPower(HardwareDeviceTypes.Intake, this.IntakePower * this.StructureBufPower);
		this.hardware.setPower(HardwareDeviceTypes.SuspensionArm, this.SuspensionArmPower * this.StructureBufPower);

		this.hardware.getDevice(HardwareDeviceTypes.Intake).update();
		this.hardware.getDevice(HardwareDeviceTypes.SuspensionArm).update();
	}

	public PositionalIntegrationMotor placementArm(){
		return (PositionalIntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.PlacementArm);
	}

	/**
	 * @param headingDeg 必须在使用driverUsingAxisPowerInsteadOfCurrentPower时给出，其他状态下给出是无效的
	 * @see Params
	 */
	public void update(final double headingDeg){
		if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower){
			this.update();
		}

		this.powersRationalize();

		this.updateDriveOptions(headingDeg);
		this.updateStructureOptions();

		if(Params.Configs.autoPrepareForNextOptionWhenUpdate){
			this.clearDriveOptions();
		}
	}
	public void update(){
		this.powersRationalize();

		this.updateDriveOptions();
		this.updateStructureOptions();

		if(Params.Configs.autoPrepareForNextOptionWhenUpdate){
			this.clearDriveOptions();
		}
	}
	
	/**
	 * @param xPoser 机器平移力
	 * @param yAxisPower 机器前行/后退力
	 * @param headingPower 机器旋转力
	 */
	public void simpleMotorPowerController(final double xPoser, final double yAxisPower, final double headingPower){
		this.LeftFrontPower += yAxisPower - xPoser - headingPower;
		this.LeftRearPower += yAxisPower + xPoser - headingPower;
		this.RightFrontPower += yAxisPower + xPoser + headingPower;
		this.RightRearPower += yAxisPower - xPoser + headingPower;

		this.powersRationalize();
	}
	public void setChassisBufPower(final double BufPower){
		this.ChassisBufPower =BufPower;
	}
	public void setStructureBufPower(final double BufPower){
		this.StructureBufPower =BufPower;
	}
	public void setBufPower(final double BudPower){
		ChassisBufPower = BudPower;
		StructureBufPower = BudPower;
	}

	private void powersRationalize(){
		this.LeftFrontPower = Mathematics.intervalClip(this.LeftFrontPower,-1,1);
		this.LeftRearPower =Mathematics.intervalClip(this.LeftRearPower,-1,1);
		this.RightFrontPower =Mathematics.intervalClip(this.RightFrontPower,-1,1);
		this.RightRearPower =Mathematics.intervalClip(this.RightRearPower,-1,1);

		this.SuspensionArmPower =Mathematics.intervalClip(this.SuspensionArmPower,-1,1);
		this.IntakePower =Mathematics.intervalClip(this.IntakePower,-1,1);
	}

	public void showPowers(){
		client.changeData("LeftFrontPower", this.LeftFrontPower);
		client.changeData("LeftRearPower", this.LeftRearPower);
		client.changeData("RightFrontPower", this.RightFrontPower);
		client.changeData("RightRearPower", this.RightRearPower);

		client.changeData("SuspensionArmPower", this.SuspensionArmPower);
		client.changeData("IntakePower", this.IntakePower);

		try{client.changeData("LeftFrontPower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.LeftFront)).getPower());}catch (
				final DeviceDisabledException ignored){}
		try{client.changeData("LeftRearPower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.LeftRear)).getPower());}catch (final
		DeviceDisabledException ignored){}
		try{client.changeData("RightFrontPower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.RightFront)).getPower());}catch (final
		DeviceDisabledException ignored){}
		try{client.changeData("RightRearPower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.RightRear)).getPower());}catch (final
		DeviceDisabledException ignored){}

		try{client.changeData("SuspensionArmPower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.SuspensionArm)).getPower());}catch (final
		DeviceDisabledException ignored){}
		try{client.changeData("IntakePower(integration)",((IntegrationMotor) this.hardware.getDevice(HardwareDeviceTypes.Intake)).getPower());}catch (final
		DeviceDisabledException ignored){}
	}
}
