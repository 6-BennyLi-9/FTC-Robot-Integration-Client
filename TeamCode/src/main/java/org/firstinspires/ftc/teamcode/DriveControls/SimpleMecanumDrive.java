package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.LinkedList;

public class SimpleMecanumDrive {
	public static final class Params{
		public static double vP=0;//用1f的力，在1s后所前行的距离，单位：inch (time(1s)*power(1f)) [sf/inch]
//		public static double kP=vP;//kP的斜率，如果您在测试中发现这两者不相等，或这kP不是恒定的，请联系我们
		public static double pem=0.5; //positionErrorMargin，单位：inch
		public static double aem=1;   //angleErrorMargin，单位：度
	}

	private final Classic classic;
	private final Motors motors;
	private final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	private Pose2d position;
	private final ImuLocalizer localizer;
	private double BufPower=1f;
	private final TelemetryPacket telemetryPacket;
	private final Client client;
	private final PID_processor pidProcessor;

	public SimpleMecanumDrive(@NonNull Classic classic, Pose2d position, Sensors sensors, Client client,
	                          PID_processor pidProcessor){
		this.classic=classic;
		this.position=position;
		this.client=client;
		motors=classic.motors;

		localizer=new ImuLocalizer(sensors);
		telemetryPacket=new TelemetryPacket();
		this.pidProcessor=pidProcessor;
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
					BufPower= Mathematics.intervalClip(BufPower,-1f,1f);
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
		drivingCommandsBuilder(){
			commands=new LinkedList<>();
			commands.add(new DriveCommands(BufPower,poseHistory.getLast()));
		}
		drivingCommandsBuilder(LinkedList<DriveCommands> commands){
			this.commands=commands;
		}
		public drivingCommandsBuilder SetPower(double power){
			power=Mathematics.intervalClip(power,-1f,1f);
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
			motors.updateDriveOptions();

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
				double progress=((et - st) / 1000.0) / estimatedTime * 100;
				client.changeDate("progress", progress +"%");
				Pose2d aim=getAimPositionThroughTrajectory(singleCommand.pose,singleCommand.NEXT(),progress);
				if(RuntimeOption.usePIDInAutonomous){
					if(Math.abs(aim.position.x-position.position.x)>Params.pem
							|| Math.abs(aim.position.y-position.position.y)>Params.pem
							|| Math.abs(aim.heading.toDouble()-position.heading.toDouble())>Params.aem
							|| RuntimeOption.alwaysRunPIDInAutonomous ){
						//间断地调用pid可能会导致pid的效果不佳
						pidProcessor.inaccuracies[0]=aim.position.x-position.position.x;
						pidProcessor.inaccuracies[1]=aim.position.y-position.position.y;
						pidProcessor.inaccuracies[2]=aim.heading.toDouble()-position.heading.toDouble();
						pidProcessor.update();

						double[] fulfillment=pidProcessor.fulfillment;

						motors.LeftFrontPower+= fulfillment[1]+fulfillment[0]-fulfillment[2];
						motors.LeftRearPower+=  fulfillment[1]-fulfillment[0]-fulfillment[2];
						motors.RightFrontPower+=fulfillment[1]-fulfillment[0]+fulfillment[2];
						motors.RightRearPower+= fulfillment[1]+fulfillment[0]+fulfillment[2];
					}
				}else{
					if(Math.abs(aim.position.x-position.position.x)>Params.pem
							|| Math.abs(aim.position.y-position.position.y)>Params.pem
							|| Math.abs(aim.heading.toDouble()-position.heading.toDouble())>Params.aem){
						double[] fulfillment=new double[]{
								(aim.position.x-position.position.x)*(Params.vP)*BufPower/2,
								(aim.position.y-position.position.y)*(Params.vP)*BufPower/2,
								(aim.heading.toDouble()>position.heading.toDouble()? BufPower/2:-BufPower/2)
						};

						motors.LeftFrontPower+= fulfillment[1]+fulfillment[0]-fulfillment[2];
						motors.LeftRearPower+=  fulfillment[1]-fulfillment[0]-fulfillment[2];
						motors.RightFrontPower+=fulfillment[1]-fulfillment[0]+fulfillment[2];
						motors.RightRearPower+= fulfillment[1]+fulfillment[0]+fulfillment[2];
					}
				}

				motors.updateDriveOptions();
			}

			client.deleteDate("distance");
			client.deleteDate("estimatedTime");
			client.deleteDate("progress");
			client.deleteDate("DELTA");
		}

		classic.STOP();
	}
	@NonNull
	@Contract ("_, _, _ -> new")
	private Pose2d getAimPositionThroughTrajectory(@NonNull Pose2d from, @NonNull Pose2d end, double progress){
		Complex cache=new Complex(new Vector2d(
				end.position.x-from.position.x,
				end.position.y-from.position.y
		));
		cache=cache.times(progress);
		return new Pose2d(
				cache.toVector2d(),
				from.heading.toDouble()+(end.heading.toDouble()-from.heading.toDouble())*progress
		);
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

		poseHistory.add(position);
	}
}
