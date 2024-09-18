package org.firstinspires.ftc.teamcode.Hardwares.Integration.Sensors;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

import java.util.ArrayDeque;
import java.util.Queue;

public class IntegrationDistanceSensor extends IntegrationSensor {
	public final DistanceSensor sensor;//REV 2M Distance Sensor
	public double CurrentMillimeterUnitDistance,SmoothMillimeterUnitDistance;
	private final Queue<Double> DistanceHistory;

	@UserRequirementFunctions
	public IntegrationDistanceSensor(@NonNull DistanceSensor sensor) {
		super(sensor.getDeviceName());
		this.sensor=sensor;
		DistanceHistory=new ArrayDeque<>();
	}

	@Override
	public void update() {
		double mergeLength=10;

		CurrentMillimeterUnitDistance =sensor.getDistance(DistanceUnit.MM);
		DistanceHistory.add(CurrentMillimeterUnitDistance);
		if(!DistanceHistory.isEmpty()&&DistanceHistory.size()>mergeLength){
			DistanceHistory.remove();
		}

		double sum=0;
		for (Double s : DistanceHistory) {
			sum += s;
		}

		SmoothMillimeterUnitDistance=sum;
	}
}
