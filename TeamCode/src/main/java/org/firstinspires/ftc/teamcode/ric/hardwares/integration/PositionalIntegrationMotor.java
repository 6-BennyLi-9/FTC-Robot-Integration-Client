package org.firstinspires.ftc.teamcode.ric.hardwares.integration;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.ric.utils.Functions;
import org.firstinspires.ftc.teamcode.ric.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.ric.utils.PID.PidProcessor;

public class PositionalIntegrationMotor extends IntegrationDevice{
	private final static double AllowErrorPosition=15;

	private boolean PID_ENABLED =true;
	private boolean LAZY_MODE = false;

	public final DcMotorEx motor;
	private final PidProcessor pidProcessor;
	private double bufPower=1f;
	private int targetPosition;

	public PositionalIntegrationMotor(@NonNull DcMotorEx motor, @NonNull HardwareDeviceTypes deviceType, PidProcessor pidProcessor){
		super(deviceType.deviceName);
		this.motor=motor;
		this.pidProcessor=pidProcessor;
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@UserRequirementFunctions
	public void initPID(int ParamID){
		if(PID_ENABLED){
			pidTag=this.getClass().getName()+"-"+motor.getDeviceName();
			pidProcessor.loadContent(new PidContent(pidTag,ParamID));
		}
	}

	@UserRequirementFunctions
	public void setTargetPosition(int position){
		targetPosition=position;
		updated=false;
	}
	@UserRequirementFunctions
	public void setBufPower(double bufPower){
		this.bufPower=bufPower;
		updated=false;
	}

	@Override
	public void update() {
		double kp = 1f;

		if(LAZY_MODE) {
			motor.setTargetPosition(targetPosition);
		}else{
			if (PID_ENABLED){
				pidProcessor.registerInaccuracies(pidTag,targetPosition-motor.getCurrentPosition());
				motor.setPower(pidProcessor.getFulfillment(pidTag));
			}else{
				motor.setPower(Functions.intervalClip(bufPower*(targetPosition-motor.getCurrentPosition())/ kp,-1,1));
			}
		}
		updated=true;

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			update();
		}
	}

	@UserRequirementFunctions
	public boolean inPlace(){
		return Math.abs(motor.getCurrentPosition() - targetPosition) < AllowErrorPosition;
	}

	@Override
	public double getPosition() {
		return motor.getCurrentPosition();
	}

	@UserRequirementFunctions
	public void setIsLazy(boolean LAZY_MODE) {
		this.LAZY_MODE = LAZY_MODE;
	}

	@UserRequirementFunctions
	public void configPidEnable(boolean val) {
		PID_ENABLED = val;
	}
}
