package org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationDevice;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceInterface;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Enums.HardwareState;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

import java.util.Map;

public class IntegrationHardwareMap {
	public Map<HardwareDevices, IntegrationDevice> devices;
	public HardwareMap lazyHardwareMap;

	public IntegrationHardwareMap(@NonNull DeviceMap map, PidProcessor processor){
		lazyHardwareMap=map.lazyHardwareMap;
		for (Map.Entry<HardwareDevices, DeviceInterface> entry : map.devices.entrySet()) {
			HardwareDevices key = entry.getKey();
			DeviceInterface value = entry.getValue();

			if(value instanceof DcMotor){
				devices.put(key,new IntegrationMotor(map,key,processor));
			}else if(value instanceof Servo){
				devices.put(key,new IntegrationMotor(map,key,processor));
			}
		}
	}

	@ExtractedInterfaces
	public IntegrationDevice getDevice(@NonNull HardwareDevices hardwareDevices){
		if(hardwareDevices.config.state== HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		if(devices.containsKey(hardwareDevices)){
			return devices.get(hardwareDevices);
		}else{
			throw new NullPointerException("Device Not Found:"+ hardwareDevices);
		}
	}
	@ExtractedInterfaces
	public void setDirection(@NonNull HardwareDevices hardwareDevices, DcMotorSimple.Direction direction){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		IntegrationDevice device=getDevice(hardwareDevices);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).motor.setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	@ExtractedInterfaces
	public void setPower(@NonNull HardwareDevices hardwareDevices, double power){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		IntegrationDevice device=getDevice(hardwareDevices);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).setPower(power);
		}else if(device instanceof IntegrationServo){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}
	@ExtractedInterfaces
	public void setPosition(@NonNull HardwareDevices hardwareDevices, double position){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		IntegrationDevice device=getDevice(hardwareDevices);
		if(device instanceof IntegrationServo){
			((IntegrationServo) device).setTargetPose(position);
		}else{
			throw new RuntimeException("Not allowed to set the position of a device witch isn't a Servo");
		}
	}
	@ExtractedInterfaces
	public double getPosition(@NonNull HardwareDevices hardwareDevices){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		IntegrationDevice device=getDevice(hardwareDevices);
		if (device instanceof IntegrationServo || device instanceof IntegrationMotor) {
			return device.getPosition();
		}else{
			throw new RuntimeException("Cannot get the position of other devices at DeviceMap.class");
		}
	}

	@ExtractedInterfaces
	public void setPowerSmooth(@NonNull HardwareDevices hardwareDevices, double power){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		IntegrationDevice device=getDevice(hardwareDevices);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).setTargetPowerSmooth(power);
		}else if(device instanceof IntegrationServo){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}
}
