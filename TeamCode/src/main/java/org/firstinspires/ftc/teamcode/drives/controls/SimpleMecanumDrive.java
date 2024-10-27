package org.firstinspires.ftc.teamcode.drives.controls;

import static org.firstinspires.ftc.teamcode.Params.aem;
import static org.firstinspires.ftc.teamcode.Params.pem;
import static org.firstinspires.ftc.teamcode.Params.timeOutProtectionMills;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.drives.controls.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.drives.controls.commands.DrivingCommandsBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.drives.localizers.plugins.DeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.annotations.DrivingPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.enums.RobotState;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

import java.util.LinkedList;

/**
 * 依赖 Global 进行初始化，注意请务必优先定义 Global
 * @see Global
 */
@DrivingPrograms
public class SimpleMecanumDrive implements DriverProgram {
	public final Chassis chassis;
	private final Motors motors;
	private final Client client;
	private final PidProcessor pidProcessor;
	private final String[] ContentTags;
	
	public final LinkedList<Position2d> poseHistory = new LinkedList<>();
	public Position2d RobotPosition;
	public double BufPower=1f;

	public final Localizer localizer;

	public static RobotState robotState;

	public SimpleMecanumDrive(Position2d RobotPosition){
		this.chassis = Global.robot.chassis;
		this.RobotPosition = RobotPosition;
		this.client=Global.client;
		SimpleMecanumDrive.robotState = Robot.robotState;
		motors= Global.robot.motors;
		this.pidProcessor=Global.robot.pidProcessor;

		//TODO:更换Localizer如果需要
		localizer=new DeadWheelLocalizer(Global.robot.sensors);

		ContentTags=new String[]{"DRIVE-X","DRIVE-Y","DRIVE-HEADING"};

		this.pidProcessor.loadContent(new PidContent(ContentTags[0], 0));
		this.pidProcessor.loadContent(new PidContent(ContentTags[1],1));
		this.pidProcessor.loadContent(new PidContent(ContentTags[2],2));
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
		PoseList[0]=commandLists[0].pose.asVector();
		Timer timer = new Timer();
		for ( int i = 0, commandListsLength = commandLists.length; i < commandListsLength; i++ ) {
			DriveCommand singleCommand = commandLists[i];
			singleCommand.RUN();
			update();
			motors.updateDriveOptions(RobotPosition.heading);

			PoseList[i+1]=singleCommand.NEXT().asVector();
			client.dashboard.DrawLine(PoseList[i],PoseList[i+1],"TargetLine");

			this.BufPower= singleCommand.BufPower;
			double dY = Math.abs(PoseList[i + 1].y - PoseList[i].y);
			double dX = Math.abs(PoseList[i + 1].x - PoseList[i].x);
			final double distance=Math.sqrt(dX * dX + dY * dY);
			final double estimatedTime=distance/(Params.secPowerPerInch /(1f/BufPower));
			client.changeData("distance",distance);
			client.changeData("estimatedTime",estimatedTime);
			client.changeData("progress","0%");
			client.changeData("DELTA",singleCommand.getDeltaTrajectory().toString());

			timer.restart();
			while ((Math.abs(RobotPosition.x - PoseList[i + 1].x) > pem)
					&& (Math.abs(RobotPosition.y - PoseList[i + 1].y) > pem)
					&& (Math.abs(RobotPosition.heading - singleCommand.NEXT().heading) > aem)) {
				double progress = (timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
				client.changeData("progress", progress + "%");
				Position2d aim = Functions.getAimPositionThroughTrajectory(singleCommand, RobotPosition, progress);

				if (timer.getDeltaTime() > estimatedTime + timeOutProtectionMills && Params.Configs.useOutTimeProtection) {//保护机制
					robotState = RobotState.BrakeDown;
					motors.updateDriveOptions();
					break;
				}

				if (Params.Configs.usePIDInAutonomous) {
					if (Math.abs(aim.x - RobotPosition.x) > pem
							|| Math.abs(aim.y - RobotPosition.y) > pem
							|| Math.abs(aim.heading - RobotPosition.heading) > aem
							|| Params.Configs.alwaysRunPIDInAutonomous) {
						//间断地调用pid可能会导致pid的效果不佳
						pidProcessor.registerInaccuracies(ContentTags[0], aim.x- RobotPosition.x);
						pidProcessor.registerInaccuracies(ContentTags[1], aim.y- RobotPosition.y);
						pidProcessor.registerInaccuracies(ContentTags[2], aim.heading- RobotPosition.heading);

						pidProcessor.update();

						motors.xAxisPower+=pidProcessor.getFulfillment(ContentTags[0]);
						motors.yAxisPower+=pidProcessor.getFulfillment(ContentTags[1]);
						motors.headingPower+=pidProcessor.getFulfillment(ContentTags[2]);
					}
				} else {
					if (Math.abs(aim.x - RobotPosition.x) > pem
							|| Math.abs(aim.y - RobotPosition.y) > pem
							|| Math.abs(aim.heading - RobotPosition.heading) > aem) {
						double[] fulfillment = new double[]{
								(aim.x - RobotPosition.x) * (Params.secPowerPerInch) * BufPower / 2,
								(aim.y - RobotPosition.y) * (Params.secPowerPerInch) * BufPower / 2,
								(aim.heading > RobotPosition.heading ? BufPower / 2 : -BufPower / 2)
						};

						motors.xAxisPower += fulfillment[0];
						motors.yAxisPower += fulfillment[1];
						motors.headingPower += fulfillment[2];
					}
				}

				motors.updateDriveOptions(RobotPosition.heading);
			}

			robotState = RobotState.WaitingAtPoint;
		}
		client.deleteData("distance");
		client.deleteData("estimatedTime");
		client.deleteData("progress");
		client.deleteData("DELTA");

		chassis.STOP();
		robotState = RobotState.IDLE;
	}

	@Override
	public Chassis getClassic() {
		return chassis;
	}

	@Override
	public Position2d getCurrentPose() {
		return RobotPosition;
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

		client.dashboard.DrawRobot(RobotPosition, DashboardClient.Blue, "RobotPosition");

		poseHistory.add(RobotPosition);
	}
}
