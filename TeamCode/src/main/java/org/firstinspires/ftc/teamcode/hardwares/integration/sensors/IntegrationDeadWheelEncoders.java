package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * 基于 Encoder 的接口性类，用于操控死轮编码器
 *
 * @see Encoder
 */
public class IntegrationDeadWheelEncoders extends IntegrationSensor{
	public final Encoder sensor;
	public double encTick,velocity;
	public double lastEncTick,lastVelocity;
	public double deltaEncTicks,deltaVelocity;

	public IntegrationDeadWheelEncoders(@NonNull DcMotorEx motor) {
		super(motor.getDeviceName());
		sensor=new OverflowEncoder(new RawEncoder(motor));
	}

	@Override
	public void update() {
		lastEncTick=encTick;
		lastVelocity=velocity;

		PositionVelocityPair pair=sensor.getPositionAndVelocity();
		encTick=pair.position;
		velocity=pair.velocity;

		deltaEncTicks=encTick-lastEncTick;
		deltaVelocity=velocity-lastVelocity;
	}
}
