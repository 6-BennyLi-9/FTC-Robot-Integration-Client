package org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices.Intake;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices.LeftFront;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices.LeftRear;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices.RightFront;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices.RightRear;
import static org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceConfigPackage.Direction.Reversed;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationDevice;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Integrations;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.PositionalIntegrationMotor;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareState;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;

import java.util.Map;

public class IntegrationHardwareMap {
	public Map<HardwareDevices, Integrations> devices;
	public HardwareMap lazyHardwareMap;

	public IntegrationHardwareMap(@NonNull HardwareMap map,PidProcessor processor){
		lazyHardwareMap=map;

		for(HardwareDevices device: HardwareDevices.values()){
			HardwareDevice object= (HardwareDevice) map.get(device.classType,device.deviceName);
			if(device.config.state==HardwareState.Disabled) continue;

			if(device.classType==DcMotorEx.class){
				DcMotorEx motor=(DcMotorEx) object;

				if(device==LeftFront||device==LeftRear||
						device==RightFront||device== RightRear||
						device==Intake){//TODO 列举需要IntegrationMotor的类
					motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

					if(device.config.direction== Reversed)motor.setDirection(Direction.REVERSE);

					devices.put(device,new IntegrationMotor(motor,device,processor,this));
				}else{

					if(device.config.direction== Reversed)motor.setDirection(Direction.REVERSE);

					devices.put(device,new PositionalIntegrationMotor(motor,device,processor));
				}
			}else if(device.classType== Servo.class){
				Servo servo=(Servo) object;

				if(device.config.direction== Reversed)servo.setDirection(Servo.Direction.REVERSE);

				devices.put(device,new IntegrationServo(servo, device));
			}else if(device.classType== BNO055IMU.class){
				BNO055IMU imu=(BNO055IMU) object;
				devices.put(device,new IntegrationBNO055(imu,device));
			}
		}
	}

	@ExtractedInterfaces
	public Integrations getDevice(@NonNull HardwareDevices hardwareDevices){
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
	public void setDirection(@NonNull HardwareDevices hardwareDevices, Direction direction){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		Integrations device=getDevice(hardwareDevices);
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
		Integrations device=getDevice(hardwareDevices);
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
		Integrations device=getDevice(hardwareDevices);
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
		Integrations device=getDevice(hardwareDevices);
		if (device instanceof IntegrationServo || device instanceof IntegrationMotor) {
			return ((IntegrationDevice) device).getPosition();
		}else{
			throw new RuntimeException("Cannot get the position of other devices at DeviceMap.class");
		}
	}

	@ExtractedInterfaces
	public void setPowerSmooth(@NonNull HardwareDevices hardwareDevices, double power){
		if(hardwareDevices.config.state==HardwareState.Disabled) {
			throw new DeviceDisabledException(hardwareDevices.name());
		}
		Integrations device=getDevice(hardwareDevices);
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
