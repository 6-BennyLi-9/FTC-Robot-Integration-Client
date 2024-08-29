package org.firstinspires.ftc.teamcode.DriveControls;

import static org.firstinspires.ftc.teamcode.Params.aem;
import static org.firstinspires.ftc.teamcode.Params.pem;
import static org.firstinspires.ftc.teamcode.Params.timeOutProtectionMills;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.DriveControls.Commands.DrivingCommandsBuilder;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.DeadWheelSubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Enums.State;
import org.firstinspires.ftc.teamcode.utils.PID_processor;
import org.firstinspires.ftc.teamcode.utils.Timer;

import java.util.LinkedList;

public class SimpleMecanumDrive implements DriverProgram{
	public final Classic classic;
	private final Motors motors;
	private final Client client;
	private final PID_processor pidProcessor;
	
	public final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	public Pose2d RobotPosition;
	public double BufPower=1f;

	public final Localizer localizer;

	public State state;

	public SimpleMecanumDrive(@NonNull Classic classic, Client client,
	                          PID_processor pidProcessor, State state, Pose2d RobotPosition){
		this.classic=classic;
		this.RobotPosition = RobotPosition;
		this.client=client;
		this.state=state;
		motors=classic.motors;

		//TODO:更换Localizer如果需要
		localizer=new DeadWheelSubassemblyLocalizer(classic);
		this.pidProcessor=pidProcessor;
	}
	public SimpleMecanumDrive(@NonNull Robot robot, Pose2d RobotPosition){
		this(robot.classic, robot.client, robot.pidProcessor, robot.state, RobotPosition);
	}

	/**
	 * @param commands 要执行的LinkedList < DriveCommand >，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runCommandPackage(@NonNull LinkedList <DriveCommand> commands){
		DriveCommand[] commandLists=new DriveCommand[commands.size()];
		commands.toArray(commandLists);
		Vector2d[] PoseList;
		PoseList=new Vector2d[commandLists.length+1];
		PoseList[0]=commandLists[0].pose.position;
		Timer timer = new Timer();
		for ( int i = 0, commandListsLength = commandLists.length; i < commandListsLength; i++ ) {
			DriveCommand singleCommand = commandLists[i];
			singleCommand.RUN();
			update();
			motors.updateDriveOptions(RobotPosition.heading.toDouble());


			PoseList[i+1]=singleCommand.NEXT().position;
			client.DrawLine(PoseList[i],PoseList[i+1]);

			this.BufPower= singleCommand.BufPower;
			double dY = Math.abs(PoseList[i + 1].y - PoseList[i].y);
			double dX = Math.abs(PoseList[i + 1].x - PoseList[i].x);
			final double distance=Math.sqrt(dX * dX + dY * dY);
			final double estimatedTime=distance/(Params.vP /(1f/BufPower));
			client.changeData("distance",distance);
			client.changeData("estimatedTime",estimatedTime);
			client.changeData("progress","0%");
			client.changeData("DELTA",singleCommand.getDeltaTrajectory().toString());

			timer.restart();
			while ((Math.abs(RobotPosition.position.x-PoseList[i+1].x)> pem)
				&& (Math.abs(RobotPosition.position.y-PoseList[i+1].y)> pem)
				&& (Math.abs(RobotPosition.heading.toDouble()-singleCommand.NEXT().heading.toDouble())> aem)){
				double progress=(timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
				client.changeData("progress", progress +"%");
				Pose2d aim=getAimPositionThroughTrajectory(singleCommand,progress);

				if(timer.getDeltaTime()>estimatedTime+ timeOutProtectionMills&& Params.Configs.useOutTimeProtection){//保护机制
					state=State.BrakeDown;
					motors.updateDriveOptions();
					break;
				}

				if(Params.Configs.usePIDInAutonomous){
					if(Math.abs(aim.position.x- RobotPosition.position.x)> pem
							|| Math.abs(aim.position.y- RobotPosition.position.y)> pem
							|| Math.abs(aim.heading.toDouble()- RobotPosition.heading.toDouble())> aem
							|| Params.Configs.alwaysRunPIDInAutonomous ){
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
					if(Math.abs(aim.position.x- RobotPosition.position.x)> pem
							|| Math.abs(aim.position.y- RobotPosition.position.y)> pem
							|| Math.abs(aim.heading.toDouble()- RobotPosition.heading.toDouble())> aem){
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

			state= State.WaitingAtPoint;
		}
		client.deleteData("distance");
		client.deleteData("estimatedTime");
		client.deleteData("progress");
		client.deleteData("DELTA");

		classic.STOP();
		state= State.IDLE;
	}

	@Override
	public Classic getClassic() {
		return classic;
	}

	/**
	 * @param driveCommandPackage 要执行的DriveCommandPackage，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runCommandPackage(@NonNull DriveCommandPackage driveCommandPackage){
		runCommandPackage(driveCommandPackage.commands);
	}
	
	/**
	 * @param from 起始点位
	 * @param end 结束点位
	 * @param progress 当前执行进度[0,1)
	 *
	 * @return 在目标进度下机器的理想位置
	 */
	@NonNull
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
	public DrivingCommandsBuilder drivingCommandsBuilder(){
		return new DrivingCommandsBuilder(this);
	}

	@Override
	public void update(){
		localizer.update();
		RobotPosition = localizer.getCurrentPose();

		client.DrawRobot(RobotPosition);

		poseHistory.add(RobotPosition);
	}
}
