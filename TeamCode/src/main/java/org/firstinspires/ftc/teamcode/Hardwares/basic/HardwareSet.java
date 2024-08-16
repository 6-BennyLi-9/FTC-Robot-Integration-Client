package org.firstinspires.ftc.teamcode.Hardwares.basic;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.enums.HardwareType;

import java.util.HashMap;
import java.util.Map;

public class HardwareSet {
	public Map<HardwareType, HardwareDevice > devices;
	public HardwareSet(){
		devices=new HashMap<>();
	}

	public void addDevice(HardwareDevice device, HardwareType hardwareType){
		devices.put(hardwareType,device);
		Log.i("HardwareSet",device.getDeviceName()+" added.");
	}
	public HardwareDevice getDevice(HardwareType hardwareType){
		if(devices.containsKey(hardwareType)){
			return devices.get(hardwareType);
		}else{
			throw new NullPointerException("Device Not Found:"+hardwareType.toString());
		}
	}
	public void setDirection(HardwareType hardwareType, DcMotorSimple.Direction direction){
		HardwareDevice device=getDevice(hardwareType);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	public void setPower(HardwareType hardwareType, double power){
		HardwareDevice device=getDevice(hardwareType);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setPower(power);
		}else if(device instanceof DcMotor){
			((DcMotor) device).setPower(power);
		}else if(device instanceof Servos){
			throw new RuntimeException("Cannot set the power of a servo at HardwareSet.class");
		}
	}
	public void setPosition(HardwareType hardwareType, double position){
		HardwareDevice device=getDevice(hardwareType);
		if(device instanceof Servo){
			((Servo) device).setPosition(position);
		}else{
			throw new RuntimeException("Not allowed to set the position of a device witch isn't a Servo");
		}
	}
	public double getPosition(HardwareType hardwareType){
		HardwareDevice device=getDevice(hardwareType);
		if (device instanceof Servo) {
			return ((Servo) device).getPosition();
		}else if(device instanceof DcMotor){
			return ((DcMotor) device).getCurrentPosition();
		}else{
			throw new RuntimeException("Cannot get the position of other devices at HardwareSet.class");
		}
	}
}
