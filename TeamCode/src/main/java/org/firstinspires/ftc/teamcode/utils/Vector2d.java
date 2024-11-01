package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

public final class Vector2d {
	public double x,y;
	public Vector2d(final double x, final double y){
		this.x=x;
		this.y=y;
	}

	@NonNull
	@Override
	public String toString() {
		return "(" + this.x + "," + "y" + ")";
	}
}
