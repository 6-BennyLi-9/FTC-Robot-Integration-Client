package org.firstinspires.ftc.teamcode.Hardwares.Namespace;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.Basic.Servos;
import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.DeviceDisabledException;

import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote OpenCvCamera和BNU055IMU都不属于接口HardwareDevice
 */
public class DeviceMap {
	public Map<HardwareDevices, DeviceInterface> devices;
	public HardwareMap lazyHardwareMap;

	public DeviceMap(HardwareMap hardwareMap){
		devices=new HashMap<>();
		register(hardwareMap);
	}

	public void register(HardwareMap hardwareMap){
		lazyHardwareMap=hardwareMap;
		for(HardwareDevices device: HardwareDevices.values()){
			if(device.getClass() ) {
				DcMotorEx hardwareDevice = (DcMotorEx) hardwareMap.get(device.classType, device.deviceName);
				if (device.config.state == HardwareState.Enabled) {
					if (device == HardwareDevices.LeftFront || device == HardwareDevices.LeftRear ||
							device == HardwareDevices.RightFront || device == HardwareDevices.RightRear) {
						hardwareDevice.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
						devices.put(device, (DeviceInterface) hardwareDevice);
					}
					if (device.config.direction == DcMotorSimple.Direction.REVERSE) {
						hardwareDevice.setDirection(DcMotorSimple.Direction.REVERSE);
					}
					devices.put(device, (DeviceInterface) hardwareMap.get(device.classType, device.deviceName));
				}
			}
		}
	}

	public HardwareDevice getDevice(@NonNull HardwareDevices hardwareDevices){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		if(devices.containsKey(hardwareDevices)){
			return devices.get(hardwareDevices);
		}else{
			throw new NullPointerException("Device Not Found:"+ hardwareDevices);
		}
	}
	public void setDirection(@NonNull HardwareDevices hardwareDevices, DcMotorSimple.Direction direction){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	public void setPower(@NonNull HardwareDevices hardwareDevices, double power){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof DcMotorEx){
			((DcMotorEx) device).setPower(power);
		}else if(device instanceof DcMotor){
			((DcMotor) device).setPower(power);
		}else if(device instanceof Servos){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}
	public void setPosition(@NonNull HardwareDevices hardwareDevices, double position){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		HardwareDevice device=getDevice(hardwareDevices);
		if(device instanceof Servo){
			((Servo) device).setPosition(position);
		}else{
			throw new RuntimeException("Not allowed to set the position of a device witch isn't a Servo");
		}
	}
	public double getPosition(@NonNull HardwareDevices hardwareDevices){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
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

	public double getVoltage(){
		return lazyHardwareMap.voltageSensor.iterator().next().getVoltage();
	}
}
