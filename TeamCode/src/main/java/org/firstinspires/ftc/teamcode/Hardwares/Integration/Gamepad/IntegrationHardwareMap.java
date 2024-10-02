package org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceConfigPackage.Direction.Reversed;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes.Intake;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes.LeftFront;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes.LeftRear;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes.RightFront;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes.RightRear;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationDevice;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Integrations;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.PositionalIntegrationMotor;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareState;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntegrationHardwareMap {
	public Map<HardwareDeviceTypes, Integrations> devices;
	private final Set<HardwareDeviceTypes> IsIntegrationMotor;
	public HardwareMap lazyHardwareMap;
	public PidProcessor lazyProcessor;

	public IntegrationHardwareMap(@NonNull HardwareMap map,PidProcessor processor){
		devices=new HashMap<>();
		lazyHardwareMap=map;
		lazyProcessor=processor;

		if(Params.Configs.autoRegisterAllHardwaresWhenInit) {
			registerAllDevices();
		}

		IsIntegrationMotor=new HashSet<>();
		//TODO 列举需要IntegrationMotor而非PositionalIntegrationMotor的类
		IsIntegrationMotor.add(LeftFront);
		IsIntegrationMotor.add(LeftRear);
		IsIntegrationMotor.add(RightFront);
		IsIntegrationMotor.add(RightRear);
		IsIntegrationMotor.add(Intake);
	}

	public void loadHardwareObject(@NonNull HardwareDeviceTypes device){
		if(device.config.state==HardwareState.Disabled)return;

		if (device.classType == DcMotor.class || device.classType == DcMotorEx.class) {
			DcMotorEx motor= lazyHardwareMap.get(DcMotorEx.class,device.deviceName);
			if(device.config.direction==Reversed){
				motor.setDirection(Direction.REVERSE);
			}
			if (IsIntegrationMotor.contains(device)){
				devices.put(device,new IntegrationMotor(motor,device,lazyProcessor,this));
			}else{
				devices.put(device,new PositionalIntegrationMotor(motor,device,lazyProcessor));
			}
		}else if (device.classType == Servo.class){
			Servo servo= lazyHardwareMap.get(Servo.class,device.deviceName);
			if(device.config.direction==Reversed){
				servo.setDirection(Servo.Direction.REVERSE);
			}
			devices.put(device,new IntegrationServo(servo,device));
		} else if (device.classType == BNO055IMU.class) {
			BNO055IMU imu= lazyHardwareMap.get(BNO055IMU.class,device.deviceName);
			devices.put(device,new IntegrationBNO055(imu,device));
		}
	}

	public void registerAllDevices(){
		for(HardwareDeviceTypes device: HardwareDeviceTypes.values()){
			loadHardwareObject(device);
		}
	}

	@ExtractedInterfaces
	public Integrations getDevice(@NonNull HardwareDeviceTypes hardwareDeviceTypes){
		if(hardwareDeviceTypes.config.state== HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		if(devices.containsKey(hardwareDeviceTypes)){
			return devices.get(hardwareDeviceTypes);
		}else{
			throw new NullPointerException("Device Not Found:"+ hardwareDeviceTypes);
		}
	}
	@ExtractedInterfaces
	public void setDirection(@NonNull HardwareDeviceTypes hardwareDeviceTypes, Direction direction){
		if(hardwareDeviceTypes.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		Integrations device=getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).motor.setDirection(direction);
		}else{
			throw new RuntimeException("DcMotor's Direction Only Matches To DcMotor");
		}
	}
	@ExtractedInterfaces
	public void setPower(@NonNull HardwareDeviceTypes hardwareDeviceTypes, double power){
		if(hardwareDeviceTypes.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		Integrations device=getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).setPower(power);
		}else if(device instanceof IntegrationServo){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}
	@ExtractedInterfaces
	public void setPosition(@NonNull HardwareDeviceTypes hardwareDeviceTypes, double position){
		if(hardwareDeviceTypes.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		Integrations device=getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationServo){
			((IntegrationServo) device).setTargetPose(position);
		}else{
			throw new RuntimeException("Not allowed to set the position of a device witch isn't a Servo");
		}
	}
	@ExtractedInterfaces
	public double getPosition(@NonNull HardwareDeviceTypes hardwareDeviceTypes){
		if(hardwareDeviceTypes.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		Integrations device=getDevice(hardwareDeviceTypes);
		if (device instanceof IntegrationServo || device instanceof IntegrationMotor) {
			return ((IntegrationDevice) device).getPosition();
		}else{
			throw new RuntimeException("Cannot get the position of other devices at DeviceMap.class");
		}
	}

	@ExtractedInterfaces
	public void setPowerSmooth(@NonNull HardwareDeviceTypes hardwareDeviceTypes, double power){
		if(hardwareDeviceTypes.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		Integrations device=getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).setTargetPowerSmooth(power);
		}else if(device instanceof IntegrationServo){
			throw new RuntimeException("Cannot set the power of a servo at DeviceMap.class");
		}
	}

	public double getVoltage(){
		return lazyHardwareMap.voltageSensor.iterator().next().getVoltage();
	}
}
