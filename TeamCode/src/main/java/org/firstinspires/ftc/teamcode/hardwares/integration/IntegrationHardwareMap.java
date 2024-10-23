package org.firstinspires.ftc.teamcode.hardwares.integration;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static org.firstinspires.ftc.teamcode.hardwares.namespace.DeviceConfigPackage.Direction.Reversed;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationEncoders;
import org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.hardwares.namespace.HardwareState;
import org.firstinspires.ftc.teamcode.hardwares.namespace.CustomizedHardwareRegisterOptions;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.Beta;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceNotFoundException;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 集成化的 hardwareMap
 */
public class IntegrationHardwareMap {
	public Map<HardwareDeviceTypes, Integrations> devices;
	private final Set<HardwareDeviceTypes> IsIntegrationMotor,IsDeadWheel;
	public HardwareMap lazyHardwareMap;
	public PidProcessor lazyProcessor;

	public IntegrationHardwareMap(@NonNull HardwareMap map,PidProcessor processor){
		devices=new HashMap<>();
		lazyHardwareMap=map;
		lazyProcessor=processor;

		IsIntegrationMotor=new HashSet<>();
		IsDeadWheel=new HashSet<>();
		//TODO 列举需要IntegrationMotor而非PositionalIntegrationMotor的类
		IsIntegrationMotor.add(HardwareDeviceTypes.LeftFront);
		IsIntegrationMotor.add(HardwareDeviceTypes.LeftRear);
		IsIntegrationMotor.add(HardwareDeviceTypes.RightFront);
		IsIntegrationMotor.add(HardwareDeviceTypes.RightRear);
		IsIntegrationMotor.add(HardwareDeviceTypes.Intake);

		//TODO 列举死轮
		IsDeadWheel.add(HardwareDeviceTypes.LeftDeadWheel);
		IsDeadWheel.add(HardwareDeviceTypes.MiddleDeadWheel);
		IsDeadWheel.add(HardwareDeviceTypes.RightDeadWheel);

		if(Params.Configs.autoRegisterAllHardwaresWhenInit) {
			registerAllDevices();
		}
	}

	public void loadHardwareObject(@NonNull HardwareDeviceTypes device){
		if(device.config.state== HardwareState.Disabled)return;

		if (device.classType == DcMotor.class || device.classType == DcMotorEx.class) {
			DcMotorEx motor= lazyHardwareMap.get(DcMotorEx.class,device.deviceName);
			if (IsIntegrationMotor.contains(device)){
				if(device.config.direction==Reversed){
					motor.setDirection(Direction.REVERSE);
				}else{
					motor.setDirection(Direction.FORWARD);
				}
				devices.put(device,new IntegrationMotor(motor,device,lazyProcessor));
			}else if(IsDeadWheel.contains(device)){
				motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

				IntegrationEncoders encoders =new IntegrationEncoders(motor);
				encoders.sensor.setDirection(Direction.REVERSE);
				devices.put(device,encoders);
			}else {
				if(device.config.direction==Reversed){
					motor.setDirection(Direction.REVERSE);
				}else{
					motor.setDirection(Direction.FORWARD);
				}
				devices.put(device,new PositionalIntegrationMotor(motor,device,lazyProcessor));
			}
		}else if (device.classType == Servo.class){
			Servo servo= lazyHardwareMap.get(Servo.class,device.deviceName);
			if(device.config.direction==Reversed){
				servo.setDirection(Servo.Direction.REVERSE);
			}else{
				servo.setDirection(Servo.Direction.FORWARD);
			}
			devices.put(device,new IntegrationServo(servo,device));
		} else if (device.classType == BNO055IMU.class) {
			BNO055IMU imu= lazyHardwareMap.get(BNO055IMU.class,device.deviceName);
			devices.put(device,new IntegrationBNO055(imu,device));
		}
	}

	@Beta
	@ExtractedInterfaces
	public void registerAllDevices(){
		for(HardwareDeviceTypes device: HardwareDeviceTypes.values()){
			if(device!=null)
				loadHardwareObject(device);
		}
	}
	@ExtractedInterfaces
	public void registerByOptions(@NonNull CustomizedHardwareRegisterOptions options){
		options.run(this);
	}

	@ExtractedInterfaces
	public Integrations getDevice(@NonNull HardwareDeviceTypes hardwareDeviceTypes){
		if(hardwareDeviceTypes.config.state== HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		if(devices.containsKey(hardwareDeviceTypes)){
			return devices.get(hardwareDeviceTypes);
		}else{
			throw new DeviceNotFoundException(hardwareDeviceTypes.deviceName);
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
			throw new RuntimeException("Not Allowed");
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
			throw new RuntimeException("Not Allowed");
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
			throw new RuntimeException("Not Allowed");
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
			throw new RuntimeException("Not Allowed");
		}
	}

	public double getVoltage(){
		return lazyHardwareMap.voltageSensor.iterator().next().getVoltage();
	}
	
	@ExtractedInterfaces
	public boolean isInPlace(HardwareDeviceTypes hardwareDeviceTypes){
		Integrations device=getDevice(hardwareDeviceTypes);
		if(device instanceof PositionalIntegrationMotor){
			return ((PositionalIntegrationMotor)device).inPlace();
		}else if(device instanceof IntegrationServo){
			return ((IntegrationServo) device).inPlace();
		}else{
			throw new RuntimeException("Not Allowed");
		}
	}

	public void printSettings(){
		for(HardwareDeviceTypes types:HardwareDeviceTypes.values()){
			Global.client.changeData(types+" direction",types.config.direction);
			Global.client.changeData(types+" state",types.config.state);
		}
	}
}
