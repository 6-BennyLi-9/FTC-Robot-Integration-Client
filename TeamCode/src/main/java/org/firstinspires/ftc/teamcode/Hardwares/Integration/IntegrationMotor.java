package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Functions;
import org.firstinspires.ftc.teamcode.Utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

public class IntegrationMotor extends IntegrationDevice{
	private boolean PID_ENABLED =true;

	public final DcMotor motor;
	private final PidProcessor pidProcessor;
	private double power=1f,lastPower;
	private final DeviceMap lazyDeviceMap;
	public double minPowerToOvercomeKineticFriction=0;
	public double minPowerToOvercomeStaticFriction=0;

	public IntegrationMotor(DeviceMap deviceMap, HardwareDevices deviceType){
		this(deviceMap,deviceType,new PidProcessor());
		Log.e("Error","PidProcessor Not Given");
	}
	public IntegrationMotor(@NonNull DeviceMap deviceMap, @NonNull HardwareDevices deviceType, PidProcessor pidProcessor){
		super(deviceType.deviceName);
		motor= (DcMotor) deviceMap.getDevice(deviceType);
		this.pidProcessor=pidProcessor;
//		mainVoltage=deviceMap.getVoltage();
		lazyDeviceMap=deviceMap;
	}

	public void initPID(int ParamID){
		if(PID_ENABLED){
			pidTag=this.getClass().getName()+"-"+motor.getDeviceName();
			pidProcessor.loadContent(new PidContent(pidTag,ParamID));
		}
	}

	public void setPower(double power){
		if(lastPower==0){
			timer.pushTimeTag("LastZeroTime");
		}
		power= Functions.intervalClip(power,-1,1);

		double m = (timer.getCurrentTime() > Params.switchFromStaticToKinetic + timer.getTimeTag("LastZeroTime") ?
				   minPowerToOvercomeKineticFriction : minPowerToOvercomeStaticFriction)
				* (12/lazyDeviceMap.getVoltage());
		power *= 1-m;

		this.power = power + m * Math.signum(power);
		updated=false;
	}

	public void setTargetPowerSmooth(double power) {
		double k = 0.7;

		if (lastPower == 0){
			timer.pushTimeTag("LastZeroTime");
		}
		if (power == 0) {
			this.power = 0;
			return;
		}
		power = Functions.intervalClip(power, -1, 1);
		double m = (System.currentTimeMillis() > Params.switchFromStaticToKinetic + timer.getTimeTag("LastZeroTime") ?
				   minPowerToOvercomeKineticFriction : minPowerToOvercomeStaticFriction)
				* (13.5/lazyDeviceMap.getVoltage());
		power *= 1-m;
		power = power + m * Math.signum(power);
		// 0.5
		this.power = power* k + this.lastPower*(1- k);
	}

	@Override
	public void update() {
		if(updated)return;//TODO:run checkout pid
		updated=true;
		if(PID_ENABLED){
			pidProcessor.registerInaccuracies(pidTag,power-motor.getPower());
			pidProcessor.ModifyPidByTag(pidTag);
			motor.setPower(motor.getPower()+pidProcessor.getFulfillment(pidTag));
		}else{
			motor.setPower(power);
		}
		timer.pushTimeTag("LastUpdateTime");
		lastPower=power;
	}

	@Override
	protected double getPosition() {
		return motor.getCurrentPosition();
	}

	@Override
	public double getPower() {
		return motor.getPower();
	}

	public void ConfigPidEnable(boolean val) {
		PID_ENABLED = val;
	}
}
