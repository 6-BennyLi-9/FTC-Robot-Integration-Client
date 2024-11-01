package org.firstinspires.ftc.teamcode.hardwares.integration.sensors;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 基于 DistanceSensor 的接口性类
 *
 * @see DistanceSensor
 */
public class IntegrationDistanceSensor extends IntegrationSensor {
	public final DistanceSensor sensor;//REV 2M Distance Sensor
	public double CurrentMillimeterUnitDistance,SmoothMillimeterUnitDistance;
	private final Queue<Double> DistanceHistory;

	@UserRequirementFunctions
	public IntegrationDistanceSensor(@NonNull final DistanceSensor sensor) {
		super(sensor.getDeviceName());
		this.sensor=sensor;
		this.DistanceHistory =new ArrayDeque<>();
	}

	@Override
	public void update() {
		final double mergeLength =10;

		this.CurrentMillimeterUnitDistance = this.sensor.getDistance(DistanceUnit.MM);
		this.DistanceHistory.add(this.CurrentMillimeterUnitDistance);
		if(! this.DistanceHistory.isEmpty() && this.DistanceHistory.size() > mergeLength){
			this.DistanceHistory.remove();
		}

		double sum=0;
		for (final Double s : this.DistanceHistory) {
			sum += s;
		}

		this.SmoothMillimeterUnitDistance =sum;
	}
}
