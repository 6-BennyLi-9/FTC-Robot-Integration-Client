package org.firstinspires.ftc.teamcode.ric.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public class Position2d {
	public double x,y, heading;
	public Position2d(double x, double y, double heading){
		this.x=x;
		this.y=y;
		this.heading = heading;
	}
	public Position2d(@NonNull Pose2d pose){
		this.x=pose.position.x;
		this.y=pose.position.y;
		this.heading = pose.heading.toDouble();
	}

	public Pose2d asPose2d(){
		return new Pose2d(x,y,heading);
	}

	@NonNull
	@Override
	public String toString() {
		return "("+x+","+"y"+"):"+heading;
	}
}