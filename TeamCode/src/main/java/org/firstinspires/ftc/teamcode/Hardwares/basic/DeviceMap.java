package org.firstinspires.ftc.teamcode.Hardwares.basic;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.enums.HardwareDevices;

import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote OpenCvCamera和BNU055IMU都不属于接口HardwareDevice
 */
public class DeviceMap {
	public Map<HardwareDevices, HardwareDevice > devices;
	public DeviceMap(HardwareMap hardwareMap){
		devices=new HashMap<>();
		for(HardwareDevices device: HardwareDevices.values()){
			devices.put(device,(HardwareDevice)hardwareMap.get(device.classType,device.deviceName));
		}
	}

	public HardwareDevice getDevice(HardwareDevices hardwareDevices){
		if(devices.containsKey(hardwareDevices)){
			return devices.get(hardwareDevices);
		}else{
			throw new NullPointerException("Device Not Found:"+ hardwareDevices.toString());
		}
	}
	public void setDirection(HardwareDevices hardwareDevices, DcMotorSimple.Direction direction){
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	public void setPower(HardwareDevices hardwareDevices, double power){
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setPower(power);
		}else if(device instanceof DcMotor){
			((DcMotor) device).setPower(power);
		}else if(device instanceof Servos){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}
	public void setPosition(HardwareDevices hardwareDevices, double position){
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof Servo){
			((Servo) device).setPosition(position);
		}else{
			throw new RuntimeException("Not allowed to set the position of a device witch isn't a Servo");
		}
	}
	public double getPosition(HardwareDevices hardwareDevices){
		HardwareDevice device=getDevice(hardwareDevices);
		if (device instanceof Servo) {
			return ((Servo) device).getPosition();
		}else if(device instanceof DcMotorEx){
			return ((DcMotorEx) device).getCurrentPosition();
		}else if(device instanceof DcMotor){
			return ((DcMotor) device).getCurrentPosition();
		}else{
			throw new RuntimeException("Cannot get the position of other devices at DeviceMap.class");
		}
	}
}
