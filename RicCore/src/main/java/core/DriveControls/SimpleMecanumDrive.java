package core.DriveControls;

import static core.Params.aem;
import static core.Params.pem;
import static core.Params.timeOutProtectionMills;
import static core.Utils.Clients.DashboardClient.Blue;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import core.DriveControls.Commands.DriveCommand;
import core.DriveControls.Commands.DrivingCommandsBuilder;
import core.DriveControls.Localizers.DeadWheelSubassemblyLocalizer;
import core.DriveControls.Localizers.LocalizerDefinition.Localizer;
import core.DriveControls.OrderDefinition.DriveOrder;
import core.DriveControls.OrderDefinition.DriveOrderPackage;
import core.DriveControls.OrderDefinition.DriverProgram;
import core.Hardwares.Classic;
import core.Hardwares.basic.Motors;
import core.Params;
import core.Robot;
import core.Utils.Annotations.DrivingPrograms;
import core.Utils.Clients.Client;
import core.Utils.Enums.State;
import core.Utils.Functions;
import core.Utils.PID_processor;
import core.Utils.Timer;

import java.util.LinkedList;

@DrivingPrograms
public class SimpleMecanumDrive implements DriverProgram {
	public final Classic classic;
	private final Motors motors;
	private final Client client;
	private final PID_processor pidProcessor;
	
	public final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	public Pose2d RobotPosition;
	public double BufPower=1f;

	public final Localizer localizer;

	public static State state;

	public SimpleMecanumDrive(@NonNull Classic classic, Client client,
	                          PID_processor pidProcessor, State state, Pose2d RobotPosition){
		this.classic=classic;
		this.RobotPosition = RobotPosition;
		this.client=client;
		SimpleMecanumDrive.state =state;
		motors=classic.motors;
		this.pidProcessor=pidProcessor;

		//TODO:更换Localizer如果需要
		localizer=new DeadWheelSubassemblyLocalizer(classic);
	}
	public SimpleMecanumDrive(@NonNull Robot robot, Pose2d RobotPosition){
		this(robot.classic, robot.client, robot.pidProcessor, robot.state, RobotPosition);
	}

	/**
	 * @param orders 要执行的LinkedList < DriveCommand >，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runOrderPackage(@NonNull LinkedList<DriveOrder> orders){
		DriveCommand[] commandLists=new DriveCommand[orders.size()];
		for (int i = 0 ; i < orders.size(); i++) {
			commandLists[i]= (DriveCommand) orders.get(i);
		}

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
			client.dashboard.DrawLine(PoseList[i],PoseList[i+1],"TargetLine");

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
			while ((Math.abs(RobotPosition.position.x - PoseList[i + 1].x) > Params.pem)
					&& (Math.abs(RobotPosition.position.y - PoseList[i + 1].y) > Params.pem)
					&& (Math.abs(RobotPosition.heading.toDouble() - singleCommand.NEXT().heading.toDouble()) > Params.aem)) {
				double progress = (timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
				client.changeData("progress", progress + "%");
				Pose2d aim = Functions.getAimPositionThroughTrajectory(singleCommand, RobotPosition, progress);

				if (timer.getDeltaTime() > estimatedTime + Params.timeOutProtectionMills && Params.Configs.useOutTimeProtection) {//保护机制
					state = State.BrakeDown;
					motors.updateDriveOptions();
					break;
				}

				if (Params.Configs.usePIDInAutonomous) {
					if (Math.abs(aim.position.x - RobotPosition.position.x) > Params.pem
							|| Math.abs(aim.position.y - RobotPosition.position.y) > Params.pem
							|| Math.abs(aim.heading.toDouble() - RobotPosition.heading.toDouble()) > Params.aem
							|| Params.Configs.alwaysRunPIDInAutonomous) {
						//间断地调用pid可能会导致pid的效果不佳
						pidProcessor.inaccuracies[0] = aim.position.x - RobotPosition.position.x;
						pidProcessor.inaccuracies[1] = aim.position.y - RobotPosition.position.y;
						pidProcessor.inaccuracies[2] = aim.heading.toDouble() - RobotPosition.heading.toDouble();
						pidProcessor.update();

						double[] fulfillment = pidProcessor.fulfillment;

						motors.xAxisPower += fulfillment[0];
						motors.yAxisPower += fulfillment[1];
						motors.headingPower += fulfillment[2];
					}
				} else {
					if (Math.abs(aim.position.x - RobotPosition.position.x) > Params.pem
							|| Math.abs(aim.position.y - RobotPosition.position.y) > Params.pem
							|| Math.abs(aim.heading.toDouble() - RobotPosition.heading.toDouble()) > Params.aem) {
						double[] fulfillment = new double[]{
								(aim.position.x - RobotPosition.position.x) * (Params.vP) * BufPower / 2,
								(aim.position.y - RobotPosition.position.y) * (Params.vP) * BufPower / 2,
								(aim.heading.toDouble() > RobotPosition.heading.toDouble() ? BufPower / 2 : -BufPower / 2)
						};

						motors.xAxisPower += fulfillment[0];
						motors.yAxisPower += fulfillment[1];
						motors.headingPower += fulfillment[2];
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
		client.dashboard.deletePacketByTag("TargetLine");

		classic.STOP();
		state= State.IDLE;
	}

	@Override
	public Classic getClassic() {
		return classic;
	}

	/**
	 * @param driveOrderPackage 要执行的DriveCommandPackage，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runOrderPackage(@NonNull DriveOrderPackage driveOrderPackage){
		runOrderPackage(driveOrderPackage.getOrder());
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

		client.dashboard.deletePacketByTag("RobotPosition");
		client.dashboard.DrawRobot(RobotPosition, Blue, "RobotPosition");

		poseHistory.add(RobotPosition);
	}
}
