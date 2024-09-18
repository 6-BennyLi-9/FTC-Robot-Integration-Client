package org.firstinspires.ftc.teamcode.Utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public class SimplePosition {
	public double x,y, heading;
	public SimplePosition(double x,double y,double heading){
		this.x=x;
		this.y=y;
		this.heading = heading;
	}
	public SimplePosition(@NonNull Pose2d pose){
		this.x=pose.position.x;
		this.y=pose.position.y;
		this.heading = pose.heading.toDouble();
	}

	public Pose2d asPose2d(){
		return new Pose2d(x,y,heading);
	}
}