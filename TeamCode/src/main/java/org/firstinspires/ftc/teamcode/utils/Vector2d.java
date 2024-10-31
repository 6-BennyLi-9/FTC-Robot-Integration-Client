package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

public final class Vector2d {
	public double x,y;
	public Vector2d(double x, double y){
		this.x=x;
		this.y=y;
	}

	@NonNull
	@Override
	public String toString() {
		return "("+x+","+"y"+")";
	}
}
