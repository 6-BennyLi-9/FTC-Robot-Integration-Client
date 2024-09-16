package org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DeviceMap;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;

public class IntegrationDeadWheelEncoders extends IntegrationSensor{
	public final Encoder sensor;
	public double encTick,velocity;

	public IntegrationDeadWheelEncoders(@NonNull DeviceMap deviceMap, @NonNull HardwareDevices deviceType) {
		super(deviceType.deviceName);
		sensor=new OverflowEncoder(new RawEncoder((DcMotorEx)deviceMap.getDevice(deviceType)));
	}

	@Override
	public void update() {
		PositionVelocityPair pair=sensor.getPositionAndVelocity();
		encTick=pair.position;
		velocity=pair.velocity;
	}
}
