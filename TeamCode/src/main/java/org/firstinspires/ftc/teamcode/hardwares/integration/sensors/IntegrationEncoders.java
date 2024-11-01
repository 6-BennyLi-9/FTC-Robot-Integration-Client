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
public class IntegrationEncoders extends IntegrationSensor{
	public final Encoder sensor;
	public double encTick,velocity;
	public double lastEncTick,lastVelocity;
	public double deltaEncTicks,deltaVelocity;

	public IntegrationEncoders(@NonNull final DcMotorEx motor) {
		super(motor.getDeviceName());
		this.sensor =new OverflowEncoder(new RawEncoder(motor));
	}

	@Override
	public void update() {
		this.lastEncTick = this.encTick;
		this.lastVelocity = this.velocity;

		final PositionVelocityPair pair = this.sensor.getPositionAndVelocity();
		this.encTick =pair.position;
		this.velocity =pair.velocity;

		this.deltaEncTicks = this.encTick - this.lastEncTick;
		this.deltaVelocity = this.velocity - this.lastVelocity;
	}
}
