package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public class Position2d {
	public double x,y, heading;
	public Position2d(double x, double y, double heading){
		this.x=x;
		this.y=y;
		this.heading = heading;
	}
	public Position2d(@NonNull Vector2d pose, double heading){
		this(pose.x,pose.y,heading);
	}

	public Pose2d asPose2d(){
		return new Pose2d(x,y,heading);
	}
	public Vector2d asVector(){
		return new Vector2d(x,y);
	}

	@NonNull
	@Override
	public String toString() {
		return "("+x+","+y+"):"+heading;
	}

	public Vector2d minus(@NonNull Vector2d pose) {
		return new Vector2d(x-pose.x,y- pose.y);
	}

	public Vector2d plus(@NonNull Vector2d pose) {
		return new Vector2d(x+ pose.x,y+ pose.y);
	}
}