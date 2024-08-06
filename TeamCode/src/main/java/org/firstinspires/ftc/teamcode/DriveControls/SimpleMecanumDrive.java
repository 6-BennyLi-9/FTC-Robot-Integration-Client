package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Twist2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;

import java.util.LinkedList;

public class SimpleMecanumDrive {
	public Classic classic;
	private final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	private Pose2d position,cache;
	private final ImuLocalizer localizer;
	private double BufPower=1f;

	public SimpleMecanumDrive(Classic classic,Pose2d position,Sensors sensors){
		this.classic=classic;
		this.position=position;

		localizer=new ImuLocalizer(sensors);
	}
	private class DriveCommands{
		private LinkedList<Pose2d> trajectory=new LinkedList<>();

		DriveCommands(){}
		DriveCommands(LinkedList<Pose2d> trajectory){
			this.trajectory=trajectory;
		}

		public void SetPower(double power){
			BufPower=power;
		}
		public void Turn(double radians){
			classic.drive(driveDirection.turn,BufPower);
			trajectory.add(trajectory.getLast().plus(new Twist2d(new Vector2d(0,0),radians)));
		}
		public void StrafeInDistance(double radians,double distance){
			classic.SimpleRadiansDrive(BufPower,radians);
			trajectory.add(trajectory.getLast().plus(new Twist2d(
					(new Complex(new Vector2d(distance,0)))
							.times(new Complex(Math.toDegrees(radians)))
							.divide(new Complex(Math.toDegrees(radians)).magnitude())
							.toVector2d()
					,radians)));
		}
		public void StrafeTo(Vector2d pose){
			Complex cache=new Complex(trajectory.getLast().position.minus(pose));
		}
	}
	public class drivingCommandsBuilder{

	}
}
