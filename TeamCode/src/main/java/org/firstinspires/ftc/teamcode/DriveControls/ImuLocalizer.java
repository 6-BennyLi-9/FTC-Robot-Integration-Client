package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;

public final class ImuLocalizer implements Localizer{
	public static class Params{
		public double X_error=0;
		public double Y_error=0;
	}
	Sensors sensors;

	ImuLocalizer(Sensors sensors){
		this.sensors=sensors;
	}

	private double LastFirstAngle,LastX,LastY;
	private boolean initialized=false;
	public Twist2dDual<Time> update() {
		if(!initialized){
			initialized=true;
			sensors.update();

			LastFirstAngle=sensors.FirstAngle;
			LastX=sensors.XMoved;
			LastY=sensors.YMoved;
			return new Twist2dDual<>(
					Vector2dDual.constant(new Vector2d(0.0, 0.0), 2),
					DualNum.constant(0.0, 2)
			);
		}
		sensors.update();
		Twist2dDual<Time> res=new Twist2dDual<>(
				new Vector2dDual<>(
						new DualNum<>(new double[]{
								sensors.XMoved - LastX,
								sensors.XMoved,
						}), new DualNum<>(new double[]{
								sensors.YMoved - LastY,
								sensors.YMoved
						})
				),
				new DualNum<>(new double[]{
						sensors.FirstAngle-LastFirstAngle,
						sensors.FirstAngle
				})
		);
		LastX=sensors.XMoved;
		LastY=sensors.YMoved;
		LastFirstAngle=sensors.FirstAngle;
		return res;
	}
}
