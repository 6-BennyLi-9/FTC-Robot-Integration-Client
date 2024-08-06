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
	public class DriveCommands{
		private double BufPower=SimpleMecanumDrive.this.BufPower;
		private LinkedList<Pose2d> trajectory=new LinkedList<>();

		DriveCommands(){}
		DriveCommands(LinkedList<Pose2d> trajectory,double BufPower){
			this.trajectory=trajectory;
			this.BufPower=BufPower;
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
			classic.SimpleRadiansDrive(BufPower,Math.toRadians(cache.toDegree()));
			trajectory.add(new Pose2d(pose,trajectory.getLast().heading));
		}
	}
	public class drivingCommandsBuilder{
		private final LinkedList < DriveCommands > commands;
		private DriveCommands cache;
		drivingCommandsBuilder(){commands=new LinkedList<>();}
		drivingCommandsBuilder(LinkedList<DriveCommands> commands){
			this.commands=commands;
		}
		public drivingCommandsBuilder SetPower(double power){
			cache=commands;
			cache.SetPower(power);
			return new drivingCommandsBuilder(cache);
		}
		public drivingCommandsBuilder TurnAngle(double angle){
			cache=commands;
			cache.Turn(angle);
			return new drivingCommandsBuilder(cache);
		}
		public drivingCommandsBuilder turn(double radians){
			return TurnAngle(Math.toDegrees(radians));
		}
		public drivingCommandsBuilder StrafeInDistance(double radians,double distance){
			cache=commands;
			cache.StrafeInDistance(radians,distance);
			return new drivingCommandsBuilder(cache);
		}
		public drivingCommandsBuilder StrafeTo(Vector2d pose){
			cache=commands;
			cache.StrafeTo(pose);
			return new drivingCommandsBuilder(cache);
		}
		public DriveCommands END(){
			return commands;
		}
	}
	public void runDriveCommand(drivingCommandsBuilder command){

	}
}
