package org.firstinspires.ftc.teamcode.Hardwares.basic;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.teamcode.utils.enums.Hardware;

import java.util.HashMap;
import java.util.Map;

public class HardwareSet {
	public Map< Hardware , HardwareDevice > devices;
	public HardwareSet(){
		devices=new HashMap<>();
	}

	public void addDevice(HardwareDevice device, Hardware hardwareType){
		devices.put(hardwareType,device);
		Log.i("HardwareSet",device.getDeviceName()+" added.");
	}
	public HardwareDevice getDevice(Hardware hardwareType){
		if(devices.containsKey(hardwareType)){
			return devices.get(hardwareType);
		}else{
			throw new NullPointerException("Device Not Found:"+hardwareType.toString());
		}
	}
	public void setDirection(Hardware hardwareType, DcMotorSimple.Direction direction){
		HardwareDevice device=getDevice(hardwareType);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	public void setPower(Hardware hardwareType,double power){
		HardwareDevice device=getDevice(hardwareType);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setPower(power);
		}else if(device instanceof DcMotor){
			((DcMotor) device).setPower(power);
		}else if(device instanceof Servos){
			throw new RuntimeException("Cannot set the power of a servo at HardwareSet.class");
		}
	}
}
