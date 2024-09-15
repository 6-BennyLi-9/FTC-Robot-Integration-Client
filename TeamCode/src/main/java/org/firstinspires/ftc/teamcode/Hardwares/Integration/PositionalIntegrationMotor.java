package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Utils.Functions;
import org.firstinspires.ftc.teamcode.Utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

public class PositionalIntegrationMotor extends IntegrationDevice{
	private final boolean PID_ENABLED =true;
	private final boolean LAZY_MODE = false;

	public final DcMotor motor;
	private final PidProcessor pidProcessor;
	private double bufPower=1f;
	private int targetPosition;

	public PositionalIntegrationMotor(DeviceMap deviceMap, HardwareDevices deviceType){
		this(deviceMap,deviceType,new PidProcessor());
		Log.e("Error","PidProcessor Not Given");
	}
	public PositionalIntegrationMotor(@NonNull DeviceMap deviceMap, @NonNull HardwareDevices deviceType, PidProcessor pidProcessor){
		super(deviceType.deviceName);
		motor= (DcMotor) deviceMap.getDevice(deviceType);
		this.pidProcessor=pidProcessor;
		if(LAZY_MODE) {
			motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}
	}

	public void initPID(int ParamID){
		if(PID_ENABLED){
			pidTag=this.getClass().getName()+"-"+motor.getDeviceName();
			pidProcessor.loadContent(new PidContent(pidTag,ParamID));
		}
	}

	public void setTargetPosition(int position){
		targetPosition=position;
	}
	public void setBufPower(double bufPower){
		this.bufPower=bufPower;
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
	}

	@Override
	protected double getPosition() {
		return motor.getCurrentPosition();
	}
}
