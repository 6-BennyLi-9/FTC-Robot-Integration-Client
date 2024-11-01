package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.jetbrains.annotations.Contract;

public final class Position2d {
	public double x,y, heading;
	public Position2d(final double x, final double y, final double heading){
		this.x=x;
		this.y=y;
		this.heading = heading;
	}
	public Position2d(@NonNull final Vector2d pose, final double heading){
		this(pose.x,pose.y,heading);
	}

	@NonNull
	@Contract(" -> new")
	public Pose2d toPose2d(){
		return new Pose2d(this.x, this.y, this.heading);
	}
	@NonNull
	@Contract(" -> new")
	public Vector2d toVector(){
		return new Vector2d(this.x, this.y);
	}

	@NonNull
	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + "):" + this.heading;
	}

	@NonNull
	@Contract("_ -> new")
	public Vector2d minus(@NonNull final Vector2d pose) {
		return new Vector2d(this.x - pose.x, this.y - pose.y);
	}

	@NonNull
	@Contract("_ -> new")
	public Vector2d plus(@NonNull final Vector2d pose) {
		return new Vector2d(this.x + pose.x, this.y + pose.y);
	}
}