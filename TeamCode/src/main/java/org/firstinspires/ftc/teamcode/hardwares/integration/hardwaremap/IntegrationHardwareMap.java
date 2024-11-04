package org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import static org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DeviceConfigPackage.Direction.Reversed;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationDevice;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.hardwares.integration.Integrations;
import org.firstinspires.ftc.teamcode.hardwares.integration.PositionalIntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationBNO055;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationDistanceSensor;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationEncoders;
import org.firstinspires.ftc.teamcode.hardwares.integration.sensors.IntegrationTouchSensor;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareState;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.CustomizedHardwareRegisterOptions;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.Beta;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceDisabledException;
import org.firstinspires.ftc.teamcode.utils.exceptions.DeviceNotFoundException;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 集成化的 hardwareMap
 * <p>
 * 支持的设备：
 * <p>
 * {@code
 * 1. DcMotorEx
 * }<p>{@code
 * 2. Servo
 * }<p>{@code
 * 3. BNO055IMU
 * }<p>{@code
 * 4. TouchSensor
 * }<p><code>
 * 5. DistanceSensor
 */
public final class IntegrationHardwareMap {
	public Map<HardwareDeviceTypes, Integrations> devices;
	private final Set<HardwareDeviceTypes>        IsIntegrationMotor,IsDeadWheel;
	public HardwareMap lazyHardwareMap;
	public PidProcessor lazyProcessor;

	public IntegrationHardwareMap(@NonNull final HardwareMap map, final PidProcessor processor){
		this.devices =new HashMap<>();
		this.lazyHardwareMap =map;
		this.lazyProcessor =processor;

		this.IsIntegrationMotor =new HashSet<>();
		this.IsDeadWheel =new HashSet<>();
		//TODO 列举需要IntegrationMotor而非PositionalIntegrationMotor的类
		this.IsIntegrationMotor.add(HardwareDeviceTypes.LeftFront);
		this.IsIntegrationMotor.add(HardwareDeviceTypes.LeftRear);
		this.IsIntegrationMotor.add(HardwareDeviceTypes.RightFront);
		this.IsIntegrationMotor.add(HardwareDeviceTypes.RightRear);
		this.IsIntegrationMotor.add(HardwareDeviceTypes.Intake);

		//TODO 列举死轮
		this.IsDeadWheel.add(HardwareDeviceTypes.LeftDeadWheel);
		this.IsDeadWheel.add(HardwareDeviceTypes.MiddleDeadWheel);
		this.IsDeadWheel.add(HardwareDeviceTypes.RightDeadWheel);

		if(Params.Configs.autoRegisterAllHardwaresWhenInit) {
			this.registerAllDevices();
		}
	}

	public void loadHardwareObject(@NonNull final HardwareDeviceTypes device){
		if(HardwareState.Disabled == device.config.state)return;

		if (DcMotor.class == device.classType || DcMotorEx.class == device.classType) {
			final DcMotorEx motor = this.lazyHardwareMap.get(DcMotorEx.class,device.deviceName);
			if (this.IsIntegrationMotor.contains(device)){
				if(Reversed == device.config.direction){
					motor.setDirection(Direction.REVERSE);
				}else{
					motor.setDirection(Direction.FORWARD);
				}
				this.devices.put(device,new IntegrationMotor(motor,device, this.lazyProcessor));
			}else if(this.IsDeadWheel.contains(device)){
				motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

				final IntegrationEncoders encoders =new IntegrationEncoders(motor);
				encoders.sensor.setDirection(Direction.REVERSE);
				this.devices.put(device,encoders);
			}else {
				if(Reversed == device.config.direction){
					motor.setDirection(Direction.REVERSE);
				}else{
					motor.setDirection(Direction.FORWARD);
				}
				this.devices.put(device,new PositionalIntegrationMotor(motor,device, this.lazyProcessor));
			}
		}else if (Servo.class == device.classType){
			final Servo servo = this.lazyHardwareMap.get(Servo.class,device.deviceName);
			if(Reversed == device.config.direction){
				servo.setDirection(Servo.Direction.REVERSE);
			}else{
				servo.setDirection(Servo.Direction.FORWARD);
			}
			this.devices.put(device,new IntegrationServo(servo,device));
		} else if (BNO055IMU.class == device.classType) {
			final BNO055IMU imu = this.lazyHardwareMap.get(BNO055IMU.class,device.deviceName);
			this.devices.put(device,new IntegrationBNO055(imu,device));
		} else if (TouchSensor.class == device.classType) {
			final TouchSensor sensor = this.lazyHardwareMap.get(TouchSensor.class,device.deviceName);
			this.devices.put(device,new IntegrationTouchSensor(sensor,device));
		}else if(DistanceSensor.class == device.classType) {
			final DistanceSensor sensor = this.lazyHardwareMap.get(DistanceSensor.class,device.deviceName);
			this.devices.put(device,new IntegrationDistanceSensor(sensor));
		}
	}

	@Beta
	@ExtractedInterfaces
	public void registerAllDevices(){
		for(final HardwareDeviceTypes device: HardwareDeviceTypes.values()){
			if(null != device) this.loadHardwareObject(device);
		}
	}
	@ExtractedInterfaces
	public void registerByOptions(@NonNull final CustomizedHardwareRegisterOptions options){
		options.run(this);
	}

	@ExtractedInterfaces
	public Integrations getDevice(@NonNull final HardwareDeviceTypes hardwareDeviceTypes){
		if(HardwareState.Disabled == hardwareDeviceTypes.config.state) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		if(this.devices.containsKey(hardwareDeviceTypes)){
			return this.devices.get(hardwareDeviceTypes);
		}else{
			throw new DeviceNotFoundException(hardwareDeviceTypes.deviceName);
		}
	}
	@ExtractedInterfaces
	public void setDirection(@NonNull final HardwareDeviceTypes hardwareDeviceTypes, final Direction direction){
		if(HardwareState.Disabled == hardwareDeviceTypes.config.state) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		final Integrations device = this.getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).motor.setDirection(direction);
		}else{
			throw new RuntimeException("Not Allowed");
		}
	}
	@ExtractedInterfaces
	public void setPower(@NonNull final HardwareDeviceTypes hardwareDeviceTypes, final double power){
		if(HardwareState.Disabled == hardwareDeviceTypes.config.state) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}

		final Integrations device = this.getDevice(hardwareDeviceTypes);

		if(device instanceof IntegrationMotor){
			((IntegrationMotor) device).setPower(power);
		}else if(device instanceof IntegrationServo){
			throw new RuntimeException("Not Allowed");
		}
	}
	@ExtractedInterfaces
	public void setPosition(@NonNull final HardwareDeviceTypes hardwareDeviceTypes, final double position){
		if(HardwareState.Disabled == hardwareDeviceTypes.config.state) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		final Integrations device = this.getDevice(hardwareDeviceTypes);
		if(device instanceof IntegrationServo){
			((IntegrationServo) device).setTargetPose(position);
		}else{
			throw new RuntimeException("Not Allowed");
		}
	}
	@ExtractedInterfaces
	public double getPosition(@NonNull final HardwareDeviceTypes hardwareDeviceTypes){
		if(HardwareState.Disabled == hardwareDeviceTypes.config.state) {
			throw new DeviceDisabledException(hardwareDeviceTypes.name());
		}
		final Integrations device = this.getDevice(hardwareDeviceTypes);
		if (device instanceof IntegrationServo || device instanceof IntegrationMotor) {
			return ((IntegrationDevice) device).getPosition();
		}else{
			throw new RuntimeException("Not Allowed");
		}
	}

	@ExtractedInterfaces
	public double getVoltage(){
		return this.lazyHardwareMap.voltageSensor.iterator().next().getVoltage();
	}
	
	@ExtractedInterfaces
	public boolean isInPlace(final HardwareDeviceTypes hardwareDeviceTypes){
		final Integrations device = this.getDevice(hardwareDeviceTypes);
		if(device instanceof PositionalIntegrationMotor){
			return ((PositionalIntegrationMotor)device).inPlace();
		}else if(device instanceof IntegrationServo){
			return ((IntegrationServo) device).inPlace();
		}else{
			throw new RuntimeException("Not Allowed");
		}
	}

	@UserRequirementFunctions
	public void printSettings(){
		for(final HardwareDeviceTypes types:HardwareDeviceTypes.values()){
			Global.client.changeData(types+" direction",types.config.direction);
			Global.client.changeData(types+" state",types.config.state);
		}
	}
}
