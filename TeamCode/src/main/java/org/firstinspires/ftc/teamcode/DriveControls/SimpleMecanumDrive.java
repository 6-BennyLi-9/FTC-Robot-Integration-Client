package org.firstinspires.ftc.teamcode.DriveControls;

import com.acmerobotics.roadrunner.Pose2d;
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
		private Pose2d DeltaTrajectory,pose;

		DriveCommands(){}
		DriveCommands(double BufPower,Pose2d pose){
			this.BufPower=BufPower;
			this.pose=pose;
		}

		public void SetPower(double power){
			BufPower=power;
		}
		public void Turn(double radians){
			classic.drive(driveDirection.turn,BufPower);
			DeltaTrajectory=new Pose2d(new Vector2d(0,0),radians);
		}
		public void StrafeInDistance(double radians,double distance){
			classic.SimpleRadiansDrive(BufPower,radians);
			DeltaTrajectory=new Pose2d(
					(new Complex(new Vector2d(distance,0))).times(new Complex(Math.toDegrees(radians)))
							.divide(new Complex(Math.toDegrees(radians)).magnitude())
							.toVector2d()
					,radians);
		}
		public void StrafeTo(Vector2d pose){
			Complex cache=new Complex(this.pose.position.minus(pose));
			classic.SimpleRadiansDrive(BufPower,Math.toRadians(cache.toDegree()));
			DeltaTrajectory=new Pose2d(cache.toVector2d(),this.pose.heading);
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
			cache=commands.getLast();
			cache.SetPower(power);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder TurnAngle(double angle){
			cache=commands.getLast();
			cache.Turn(angle);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder turn(double radians){
			return TurnAngle(Math.toDegrees(radians));
		}
		public drivingCommandsBuilder StrafeInDistance(double radians,double distance){
			cache=commands.getLast();
			cache.StrafeInDistance(radians,distance);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder StrafeTo(Vector2d pose){
			cache=commands.getLast();
			cache.StrafeTo(pose);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public LinkedList < DriveCommands > END(){
			return commands;
		}
	}
	public void runDriveCommand(LinkedList < DriveCommands > commands){

	}
}
