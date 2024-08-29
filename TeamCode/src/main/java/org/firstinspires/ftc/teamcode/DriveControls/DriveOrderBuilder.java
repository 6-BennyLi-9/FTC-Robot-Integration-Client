package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;

public interface DriveOrderBuilder {
	DriveOrderBuilder SetPower(double power);
	DriveOrderBuilder TurnRadians(double radians);
	DriveOrderBuilder TurnAngle(double deg);
	DriveOrderBuilder StrafeInDistance(double radians, double distance);
	DriveOrderBuilder StrafeTo(Vector2d pose);
	DriveCommandPackage END();
}
