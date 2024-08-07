package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;

import java.util.Arrays;
import java.util.LinkedList;

public class SimpleMecanumDrive {
	public static final class Params{
		public static double vP=0;//用1f的力，在1s后所前行的距离，单位：inch
//		public static double kP=vP;//kP的斜率，如果您在测试中发现这两者不相等，或这kP不是恒定的，请联系我们
		public static double pem=0.5; //positionErrorMargin，单位：inch
		public static double aem=1;   //angleErrorMargin，单位：度
	}

	public Classic classic;
	private final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	private Pose2d position;
	private final ImuLocalizer localizer;
	private double BufPower=1f;
	private final TelemetryPacket telemetryPacket;
	private final Client client;

	public SimpleMecanumDrive(Classic classic,Pose2d position,Sensors sensors,Client client){
		this.classic=classic;
		this.position=position;
		this.client=client;

		localizer=new ImuLocalizer(sensors);
		telemetryPacket=new TelemetryPacket();
	}
	public class DriveCommands{
		public abstract class commandRunningNode{
			public void runCommand() {}
		}

		private double BufPower;
		private Pose2d DeltaTrajectory;
		private final Pose2d pose;
		public commandRunningNode MEAN;

		DriveCommands(double BufPower,Pose2d pose){
			this.BufPower=BufPower;
			this.pose=pose;
		}

		public void SetPower(double power){
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					BufPower=power;
				}
			};
		}
		public void Turn(double radians){
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					classic.drive(driveDirection.turn,BufPower);
				}
			};
			DeltaTrajectory=new Pose2d(new Vector2d(0,0),radians);
		}
		public void StrafeInDistance(double radians,double distance){
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					classic.SimpleRadiansDrive(BufPower,radians);
				}
			};
			DeltaTrajectory=new Pose2d(
					(new Complex(new Vector2d(distance,0))).times(new Complex(Math.toDegrees(radians)))
							.divide(new Complex(Math.toDegrees(radians)).magnitude())
							.toVector2d()
					,radians);
		}
		public void StrafeTo(Vector2d pose){
			Complex cache=new Complex(this.pose.position.minus(pose));
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					classic.SimpleRadiansDrive(BufPower,Math.toRadians(cache.toDegree()));
				}
			};
			DeltaTrajectory=new Pose2d(cache.toVector2d(),this.pose.heading);
		}
		public void RUN(){
			MEAN.runCommand();
		}
		public Pose2d getDeltaTrajectory(){
			return DeltaTrajectory;
		}
		public Pose2d NEXT(){
			return new Pose2d(
					pose.position.x+DeltaTrajectory.position.x,
					pose.position.y+DeltaTrajectory.position.y,
					pose.heading.toDouble()+DeltaTrajectory.heading.toDouble()
					);
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
			cache=new DriveCommands(commands.getLast().BufPower,commands.getLast().NEXT());
			cache.SetPower(power);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder TurnAngle(double angle){
			cache=new DriveCommands(commands.getLast().BufPower,commands.getLast().NEXT());
			cache.Turn(angle);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder turn(double radians){
			return TurnAngle(Math.toDegrees(radians));
		}
		public drivingCommandsBuilder StrafeInDistance(double radians,double distance){
			cache=new DriveCommands(commands.getLast().BufPower,commands.getLast().NEXT());
			cache.StrafeInDistance(radians,distance);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public drivingCommandsBuilder StrafeTo(Vector2d pose){
			cache=new DriveCommands(commands.getLast().BufPower,commands.getLast().NEXT());
			cache.StrafeTo(pose);
			commands.add(cache);
			return new drivingCommandsBuilder(commands);
		}
		public LinkedList < DriveCommands > END(){
			return commands;
		}
	}
	public void runDriveCommand(@NonNull LinkedList < DriveCommands > commands){
		Pose2d aim=position;
		DriveCommands[] commandLists=new DriveCommands[commands.size()];
		commands.toArray(commandLists);
		double[] xList,yList;
		xList=new double[commandLists.length+1];
		yList=new double[commandLists.length+1];
		xList[0]=commandLists[0].pose.position.x;
		yList[0]=commandLists[0].pose.position.y;
		long st,et;
		for ( int i = 0, commandListsLength = commandLists.length; i < commandListsLength; i++ ) {
			DriveCommands singleCommand = commandLists[i];
			singleCommand.RUN();
			update();
			aim=singleCommand.pose;
			Canvas c = telemetryPacket.fieldOverlay();
			c.setStroke("#4CAF50");

			xList[i+1]=singleCommand.NEXT().position.x;
			yList[i+1]=singleCommand.NEXT().position.y;

			c.strokePolyline(
					Arrays.copyOf(xList,i+1),
					Arrays.copyOf(yList,i+1)
			);
			this.BufPower= singleCommand.BufPower;
			final double distance=Math.sqrt(
					Math.abs(xList[i+1]-xList[i])*Math.abs(xList[i+1]-xList[i])+
							Math.abs(yList[i+1]-yList[i])*Math.abs(yList[i+1]-yList[i])
			);
			final double estimatedTime=distance/(Params.vP/(1f/BufPower));
			client.addData("distance",String.valueOf(distance));
			client.addData("estimatedTime",String.valueOf(estimatedTime));
			client.addData("progress","0%");
			client.addData("DELTA",singleCommand.getDeltaTrajectory().toString());

			st=System.currentTimeMillis();
			while ((Math.abs(position.position.x-xList[i+1])>Params.pem)
				&& (Math.abs(position.position.y-yList[i+1])>Params.pem)
				&& (Math.abs(position.heading.toDouble()-singleCommand.NEXT().heading.toDouble())>Params.aem)){
				et=System.currentTimeMillis();
				client.changeDate("progress", ((et - st) / 1000.0) / estimatedTime * 100 +"%");
			}
		}
	}
	public drivingCommandsBuilder drivingCommandsBuilder(){
		return new drivingCommandsBuilder();
	}

	public void update(){
		Twist2dDual<Time> localizerPosition = localizer.update();
		position=new Pose2d(
				localizerPosition.line.x.get(1),
				localizerPosition.line.y.get(1),
				localizerPosition.angle.get(1)
				);

		Canvas c=telemetryPacket.fieldOverlay();
		c.setStroke("#3F51B5");
		Drawing.drawRobot(c,position);
	}

	public double getBufPower() {
		return BufPower;
	}
}
