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
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.enums.State;
import org.firstinspires.ftc.teamcode.utils.enums.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.LinkedList;

public class SimpleMecanumDrive {
	public static final class Params{
		/**
		 * 用1f的力，在1s后所前行的距离，单位：inch (time(1s)*power(1f)) [sf/inch]
		 */
		public static double vP=0;
		//kP的斜率，如果您在测试中发现这两者不相等，或者kP不是恒定的，请联系我们
//		public static double kP=vP;
		/**
		 *positionErrorMargin，单位：inch
		 */
		public static double pem=0.5;
		/**
		 *angleErrorMargin，单位：度
		 */
		public static double aem=1;
		/**
		 * 机器的超时保护机制，如果超过该时间，机器仍未到达点位，则会强制取消点位的执行
		 */
		public static double timeOutProtectionMills=1000;
	}

	private final Classic classic;
	private final Motors motors;
	private final Client client;
	private final PID_processor pidProcessor;
	private final TelemetryPacket telemetryPacket;
	
	private final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	private Pose2d RobotPosition;
	private final ImuLocalizer localizer;
	private double BufPower=1f;

	public State state;

	public SimpleMecanumDrive(@NonNull Classic classic, Client client,
	                          PID_processor pidProcessor, State state, Pose2d RobotPosition){
		this.classic=classic;
		this.RobotPosition = RobotPosition;
		this.client=client;
		this.state=state;
		motors=classic.motors;

		localizer=new ImuLocalizer(classic.sensors);
		telemetryPacket=new TelemetryPacket();
		this.pidProcessor=pidProcessor;
	}
	public SimpleMecanumDrive(@NonNull Robot robot, Pose2d RobotPosition){
		this(robot.classic, robot.client, robot.pidProcessor, robot.state, RobotPosition);
	}
	public class DriveCommand {
		/**
		 * 为了简化代码书写，我们使用了<code>@Override</code>的覆写来保存数据。
		 * <p>如果使用enum，则代码会明显过于臃肿</p>
		 */
		public abstract class commandRunningNode{
			public void runCommand() {}
		}
		
		public commandRunningNode MEAN;
		private double BufPower;
		private Pose2d DeltaTrajectory;
		private final Pose2d pose;
		/**
		 * <code>面向开发者：</code> 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
		 */
		public TrajectoryType trajectoryType=null;

		DriveCommand(double BufPower, Pose2d pose){
			this.BufPower=BufPower;
			this.pose=pose;
		}
		
		/**
		 * 在该节点只修改电机BufPower，不会在定义时影响主程序
		 * @param power 目标设置的电机BufPower
		 */
		public void SetPower(double power){
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					BufPower=power;
					BufPower= Mathematics.intervalClip(BufPower,-1f,1f);
				}
			};
		}
		
		/**
		 * 在该节点让机器旋转指定弧度
		 * @param radians 要转的弧度
		 */
		public void Turn(double radians){
			MEAN=new commandRunningNode() {
				@Override
				public void runCommand() {
					classic.drive(driveDirection.turn,BufPower);
				}
			};
			DeltaTrajectory=new Pose2d(new Vector2d(0,0),radians);
		}
		
		/**
		 * 在该节点让机器在指定角度行驶指定距离
		 * @param radians 相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
		 * @param distance 要行驶的距离
		 */
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
		
		/**
		 * 在该节点让机器在不旋转的情况下平移
		 * @param pose 目标矢量点位
		 */
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
		
		/**
		 * 不要在自动程序中调用这个函数，否则你会后悔的
		 */
		public void RUN(){
			MEAN.runCommand();
		}
		public Pose2d getDeltaTrajectory(){
			return DeltaTrajectory;
		}
		
		/**
		 * @return 该Command节点的目标点位
		 */
		@NonNull
		@Contract(" -> new")
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
		
		/**
		 * 在该节点只修改电机BufPower，不会在定义时影响主程序
		 * @param power 目标设置的电机BufPower
		 */
		public drivingCommandsBuilder SetPower(double power){
			power=Mathematics.intervalClip(power,-1f,1f);
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower,commandPackage.commands.getLast().NEXT());
			cache.SetPower(power);
			cache.trajectoryType=TrajectoryType.WithoutChangingPosition;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		/**
		 * 在该节点让机器旋转指定弧度
		 * @param radians 要转的弧度[-PI,PI)
		 */
		public drivingCommandsBuilder TurnRadians(double radians){
			radians=Mathematics.intervalClip(radians,-Math.PI,Math.PI);
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower,commandPackage.commands.getLast().NEXT());
			cache.Turn(radians);
			cache.trajectoryType=TrajectoryType.TurnOnly;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		/**
		 * 在该节点让机器旋转指定角度
		 * @param deg 要转的角度[-180,180)
		 */
		public drivingCommandsBuilder TurnAngle(double deg){
			return TurnRadians(Math.toRadians(deg));
		}
		/**
		 * 在该节点让机器在指定角度行驶指定距离
		 * @param radians 相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
		 * @param distance 要行驶的距离
		 */
		public drivingCommandsBuilder StrafeInDistance(double radians,double distance){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.StrafeInDistance(radians,distance);
			cache.trajectoryType=TrajectoryType.LinerStrafe;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		/**
		 * 在该节点让机器在不旋转的情况下平移
		 * @param pose 目标矢量点位
		 */
		public drivingCommandsBuilder StrafeTo(Vector2d pose){
			cache=new DriveCommand(commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
			cache.StrafeTo(pose);
			cache.trajectoryType=TrajectoryType.LinerStrafe;
			commandPackage.commands.add(cache);
			return new drivingCommandsBuilder(commandPackage);
		}
		/**
		 * 结束该DriveCommandPackage
		 */
		public DriveCommandPackage END(){
			return commandPackage;
		}
	}
	
	/**
	 * 为了方便存储，查询
	 */
	public class DriveCommandPackage{
		public LinkedList < DriveCommand > commands;
		DriveCommandPackage(){
			commands=new LinkedList<>();
		}
	}
	
	/**
	 * @param commands 要执行的LinkedList < DriveCommand >，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
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
			motors.updateDriveOptions(RobotPosition.heading.toDouble());

			Canvas c = telemetryPacket.fieldOverlay();
			c.setStroke("#4CAF50");

			xList[i+1]=singleCommand.NEXT().position.x;
			yList[i+1]=singleCommand.NEXT().position.y;

			c.strokePolyline(
					Arrays.copyOf(xList, i + 1),
					Arrays.copyOf(yList, i + 1)
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

				if(et>st+estimatedTime+Params.timeOutProtectionMills&&RuntimeOption.useOutTimeProtection){//保护机制
					state=State.BrakeDown;
					motors.updateDriveOptions();
					break;
				}

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
						
						motors.xAxisPower+=fulfillment[0];
						motors.yAxisPower+=fulfillment[1];
						motors.headingPower+=fulfillment[2];
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

						motors.xAxisPower+=fulfillment[0];
						motors.yAxisPower+=fulfillment[1];
						motors.headingPower+=fulfillment[2];
					}
				}

				motors.updateDriveOptions(RobotPosition.heading.toDouble());
			}

			client.deleteDate("distance");
			client.deleteDate("estimatedTime");
			client.deleteDate("progress");
			client.deleteDate("DELTA");
			state= State.WaitingAtPoint;
		}

		classic.STOP();
		state= State.IDLE;
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
		cache=cache.times(progress);
		return new Pose2d(
				RobotPosition.position.x+cache.RealPart,
				RobotPosition.position.y+cache.imaginary(),
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
				state= State.StrafeToPoint;
				return getAimPositionThroughTrajectory(driveCommand.pose, driveCommand.NEXT(), progress);
			case Spline://TODO:功能仍在开发中
				state= State.FollowSpline;
				break;
			default:
				return new Pose2d(0, 0, 0);
		}
		throw new RuntimeException("If you see this Exception on DriverHub, please let us know in the issue");
	}
	
	/**
	 * @return 定义开启新的drivingCommandsBuilder
	 */
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
}
