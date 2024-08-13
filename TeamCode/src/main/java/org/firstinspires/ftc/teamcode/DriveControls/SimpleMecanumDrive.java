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
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.enums.TrajectoryType;
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
	private Pose2d RobotPosition;
	private final ImuLocalizer localizer;
	private double BufPower=1f;
	private final TelemetryPacket telemetryPacket;
	private final Client client;
	private final PID_processor pidProcessor;

	public SimpleMecanumDrive(@NonNull Classic classic, Pose2d RobotPosition, Sensors sensors, Client client,
	                          PID_processor pidProcessor){
		this.classic=classic;
		this.RobotPosition = RobotPosition;
		this.client=client;
		motors=classic.motors;

		localizer=new ImuLocalizer(sensors);
		telemetryPacket=new TelemetryPacket();
		this.pidProcessor=pidProcessor;
	}
	public class DriveCommand {
		public abstract class commandRunningNode{
			public void runCommand() {}
		}

		private double BufPower;
		private Pose2d DeltaTrajectory;
		private final Pose2d pose;
		public commandRunningNode MEAN;
		/**
		 * <code>面向开发者：</code> 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
		 */
		public TrajectoryType trajectoryType=null;

		DriveCommand(double BufPower, Pose2d pose){
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
		private final DriveCommandPackage commandPackage;
		private DriveCommand cache;
		drivingCommandsBuilder(){
			commandPackage =new DriveCommandPackage();
			commandPackage.commands.add(new DriveCommand(BufPower,poseHistory.getLast()));
		}
		drivingCommandsBuilder(DriveCommandPackage commandPackage){
			this.commandPackage = commandPackage;
		}
		public drivingCommandsBuilder SetPower(double power){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.SetPower(power);
			cache.trajectoryType=TrajectoryType.WithoutChangingPosition;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		public drivingCommandsBuilder TurnAngle(double angle){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.Turn(angle);
			cache.trajectoryType=TrajectoryType.TurnOnly;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		public drivingCommandsBuilder turn(double radians){
			return TurnAngle(Math.toDegrees(radians));
		}
		public drivingCommandsBuilder StrafeInDistance(double radians,double distance){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.StrafeInDistance(radians,distance);
			cache.trajectoryType=TrajectoryType.LinerStrafe;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		public drivingCommandsBuilder StrafeTo(Vector2d pose){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.StrafeTo(pose);
			cache.trajectoryType=TrajectoryType.LinerStrafe;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		public DriveCommandPackage END(){
			return commandPackage;
		}
	}
	public class DriveCommandPackage{
		public LinkedList < DriveCommand > commands;
		DriveCommandPackage(){
			commands=new LinkedList<>();
		}
	}
	public void runDriveCommands(@NonNull LinkedList < DriveCommand > commands){
		DriveCommand[] commandLists=new DriveCommand[commands.size()];
		commands.toArray(commandLists);
		double[] xList,yList;
		xList=new double[commandLists.length+1];
		yList=new double[commandLists.length+1];
		xList[0]=commandLists[0].pose.position.x;
		yList[0]=commandLists[0].pose.position.y;
		long st,et;
		for ( int i = 0, commandListsLength = commandLists.length; i < commandListsLength; i++ ) {
			DriveCommand singleCommand = commandLists[i];
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
			while ((Math.abs(RobotPosition.position.x-xList[i+1])>Params.pem)
				&& (Math.abs(RobotPosition.position.y-yList[i+1])>Params.pem)
				&& (Math.abs(RobotPosition.heading.toDouble()-singleCommand.NEXT().heading.toDouble())>Params.aem)){
				et=System.currentTimeMillis();
				double progress=((et - st) / 1000.0) / estimatedTime * 100;
				client.changeDate("progress", progress +"%");
				Pose2d aim=getAimPositionThroughTrajectory(singleCommand,progress);
				
				if(RuntimeOption.usePIDInAutonomous){
					if(Math.abs(aim.position.x- RobotPosition.position.x)>Params.pem
							|| Math.abs(aim.position.y- RobotPosition.position.y)>Params.pem
							|| Math.abs(aim.heading.toDouble()- RobotPosition.heading.toDouble())>Params.aem
							|| RuntimeOption.alwaysRunPIDInAutonomous ){
						//间断地调用pid可能会导致pid的效果不佳
						pidProcessor.inaccuracies[0]=aim.position.x- RobotPosition.position.x;
						pidProcessor.inaccuracies[1]=aim.position.y- RobotPosition.position.y;
						pidProcessor.inaccuracies[2]=aim.heading.toDouble()- RobotPosition.heading.toDouble();
						pidProcessor.update();

						double[] fulfillment=pidProcessor.fulfillment;

						motors.LeftFrontPower+= fulfillment[1]+fulfillment[0]-fulfillment[2];
						motors.LeftRearPower+=  fulfillment[1]-fulfillment[0]-fulfillment[2];
						motors.RightFrontPower+=fulfillment[1]-fulfillment[0]+fulfillment[2];
						motors.RightRearPower+= fulfillment[1]+fulfillment[0]+fulfillment[2];
					}
				}else{
					if(Math.abs(aim.position.x- RobotPosition.position.x)>Params.pem
							|| Math.abs(aim.position.y- RobotPosition.position.y)>Params.pem
							|| Math.abs(aim.heading.toDouble()- RobotPosition.heading.toDouble())>Params.aem){
						double[] fulfillment=new double[]{
								(aim.position.x- RobotPosition.position.x)*(Params.vP)*BufPower/2,
								(aim.position.y- RobotPosition.position.y)*(Params.vP)*BufPower/2,
								(aim.heading.toDouble()> RobotPosition.heading.toDouble()? BufPower/2:-BufPower/2)
						};

						motors.LeftFrontPower+= fulfillment[1]+fulfillment[0]-fulfillment[2];
						motors.LeftRearPower+=  fulfillment[1]-fulfillment[0]-fulfillment[2];
						motors.RightFrontPower+=fulfillment[1]-fulfillment[0]+fulfillment[2];
						motors.RightRearPower+= fulfillment[1]+fulfillment[0]+fulfillment[2];
					}
				}
			}
		}

		classic.STOP();
	}
	
	/**
	 * @param driveCommandPackage 要执行的DriveCommandPackage，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	public void runDriveCommandPackage(@NonNull DriveCommandPackage driveCommandPackage){
		runDriveCommands(driveCommandPackage.commands);
	}
	
	/**
	 * @param from 起始点位
	 * @param end 结束点位
	 * @param progress 当前执行进度[0,1)
	 *
	 * @return 在目标进度下机器的理想位置
	 */
	@NonNull
	@Contract ("_, _, _ -> new")
	private Pose2d getAimPositionThroughTrajectory(@NonNull Pose2d from, @NonNull Pose2d end, double progress){
		Complex cache=new Complex(new Vector2d(
				end.position.x-from.position.x,
				end.position.y-from.position.y
		));
		cache.times(progress);
		return new Pose2d(
				RobotPosition.position.x+cache.RealPart,
				RobotPosition.position.y+cache.ImaginaryPart.factor,
				from.heading.toDouble()+(end.heading.toDouble()-from.heading.toDouble())*progress
		);
	}
	
	/**
	 * @param driveCommand 给出的执行命令，会自动根据给出的命令进行判断处理
	 * @param progress 当前执行进度[0,1)
	 *
	 * @return 在目标进度下机器的理想位置
	 */
	@NonNull
	private Pose2d getAimPositionThroughTrajectory(@NonNull DriveCommand driveCommand, double progress){
		switch (driveCommand.trajectoryType) {
			case LinerStrafe:
			case LinerWithTurn:
			case TurnOnly:
				return getAimPositionThroughTrajectory(driveCommand.pose, driveCommand.NEXT(), progress);
			case Spline://TODO:功能仍在开发中
				
				break;
			default:
				return new Pose2d(0, 0, 0);
		}
		throw new RuntimeException("If you see this Exception on DriverHub, please let us know in the issue");
	}
	public drivingCommandsBuilder drivingCommandsBuilder(){
		return new drivingCommandsBuilder();
	}

	public void update(){
		Twist2dDual<Time> localizerPosition = localizer.update();
		RobotPosition =new Pose2d(
				localizerPosition.line.x.get(1),
				localizerPosition.line.y.get(1),
				localizerPosition.angle.get(1)
				);

		Canvas c=telemetryPacket.fieldOverlay();
		c.setStroke("#3F51B5");
		Drawing.drawRobot(c, RobotPosition);

		poseHistory.add(RobotPosition);
	}

	public double getBufPower() {
		return BufPower;
	}
}
