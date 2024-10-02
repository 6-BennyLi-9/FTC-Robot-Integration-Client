package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Functions;
import org.firstinspires.ftc.teamcode.Utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

public class PositionalIntegrationMotor extends IntegrationDevice{
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
	public void ConfigPidEnable(boolean val) {
		PID_ENABLED = val;
	}
}
