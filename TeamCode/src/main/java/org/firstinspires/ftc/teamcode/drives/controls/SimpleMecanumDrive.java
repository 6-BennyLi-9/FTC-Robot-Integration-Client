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
import org.firstinspires.ftc.teamcode.hardwares.controllers.Motors;
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
	public double BufPower= 1.0f;

	public final Localizer localizer;

	public static RobotState robotState;

	public SimpleMecanumDrive(final Position2d RobotPosition){
		chassis = Global.robot.chassis;
		this.RobotPosition = RobotPosition;
		client=Global.client;
		robotState = Robot.robotState;
		this.motors = Global.robot.motors;
		pidProcessor=Global.robot.pidProcessor;

		//TODO:更换Localizer如果需要
		this.localizer =new DeadWheelLocalizer(Global.robot.sensors);

		this.ContentTags =new String[]{"DRIVE-X", "DRIVE-Y", "DRIVE-HEADING"};

		pidProcessor.loadContent(new PidContent(this.ContentTags[0], 0));
		pidProcessor.loadContent(new PidContent(this.ContentTags[1],1));
		pidProcessor.loadContent(new PidContent(this.ContentTags[2],2));

		this.poseHistory.push(RobotPosition);
	}

	/**
	 * @param orders 要执行的LinkedList < DriveCommand >，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runOrderPackage(@NonNull final LinkedList<DriveOrder> orders){
		final DriveCommand[] commandLists =new DriveCommand[orders.size()];
		for (int i = 0 ; i < orders.size(); i++) {
			commandLists[i]= (DriveCommand) orders.get(i);
		}

		final Vector2d[] PoseList;
		PoseList=new Vector2d[commandLists.length+1];
		PoseList[0]=commandLists[0].pose.toVector();
		final Timer timer = new Timer();
		for ( int i = 0, commandListsLength = commandLists.length; i < commandListsLength; i++ ) {
			final DriveCommand singleCommand = commandLists[i];
			singleCommand.run();
			this.update();
			this.motors.updateDriveOptions(this.RobotPosition.heading);

			PoseList[i+1]=singleCommand.nextPose().toVector();
			this.client.dashboard.drawLine(PoseList[i],PoseList[i + 1],"TargetLine");

			BufPower= singleCommand.BufPower;
			final double dY = Math.abs(PoseList[i + 1].y - PoseList[i].y);
			final double dX = Math.abs(PoseList[i + 1].x - PoseList[i].x);
			double       distance =Math.sqrt(dX * dX + dY * dY);
			double estimatedTime=distance/(Params.secPowerPerInch /(1.0f / this.BufPower));
			this.client.changeData("distance",distance);
			this.client.changeData("estimatedTime",estimatedTime);
			this.client.changeData("progress","0%");
			this.client.changeData("DELTA",singleCommand.getDeltaTrajectory().toString());

			timer.restart();
			while ((Math.abs(this.RobotPosition.x - PoseList[i + 1].x) > pem)
					&& (Math.abs(this.RobotPosition.y - PoseList[i + 1].y) > pem)
					&& (Math.abs(this.RobotPosition.heading - singleCommand.nextPose().heading) > aem)) {
				final double progress = (timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
				this.client.changeData("progress", progress + "%");
				final Position2d aim = Functions.getAimPositionThroughTrajectory(singleCommand, this.RobotPosition, progress);

				if (timer.getDeltaTime() > estimatedTime + timeOutProtectionMills && Params.Configs.useOutTimeProtection) {//保护机制
					SimpleMecanumDrive.robotState = RobotState.BrakeDown;
					this.motors.updateDriveOptions();
					break;
				}

				if (Params.Configs.usePIDToDriveInAutonomous) {
					if (Math.abs(aim.x - this.RobotPosition.x) > pem
							|| Math.abs(aim.y - this.RobotPosition.y) > pem
							|| Math.abs(aim.heading - this.RobotPosition.heading) > aem
							|| Params.Configs.alwaysRunPIDInAutonomous) {
						//间断地调用pid可能会导致pid的效果不佳
						this.pidProcessor.registerInaccuracies(this.ContentTags[0], aim.x - this.RobotPosition.x);
						this.pidProcessor.registerInaccuracies(this.ContentTags[1], aim.y - this.RobotPosition.y);
						this.pidProcessor.registerInaccuracies(this.ContentTags[2], aim.heading - this.RobotPosition.heading);

						this.pidProcessor.update();

						this.motors.xAxisPower+= this.pidProcessor.getFulfillment(this.ContentTags[0]);
						this.motors.yAxisPower+= this.pidProcessor.getFulfillment(this.ContentTags[1]);
						this.motors.headingPower+= this.pidProcessor.getFulfillment(this.ContentTags[2]);
					}
				} else {
					if (Math.abs(aim.x - this.RobotPosition.x) > pem
							|| Math.abs(aim.y - this.RobotPosition.y) > pem
							|| Math.abs(aim.heading - this.RobotPosition.heading) > aem) {
						final double[] fulfillment = {
								(aim.x - this.RobotPosition.x) * (Params.secPowerPerInch) * this.BufPower / 2,
								(aim.y - this.RobotPosition.y) * (Params.secPowerPerInch) * this.BufPower / 2,
								(aim.heading > this.RobotPosition.heading ? this.BufPower / 2 : - this.BufPower / 2)
						};

						this.motors.xAxisPower += fulfillment[0];
						this.motors.yAxisPower += fulfillment[1];
						this.motors.headingPower += fulfillment[2];
					}
				}

				this.motors.updateDriveOptions(this.RobotPosition.heading);
			}

			SimpleMecanumDrive.robotState = RobotState.WaitingAtPoint;
		}
		this.client.deleteData("distance");
		this.client.deleteData("estimatedTime");
		this.client.deleteData("progress");
		this.client.deleteData("DELTA");

		this.chassis.STOP();
		SimpleMecanumDrive.robotState = RobotState.IDLE;
	}

	@Override
	public Chassis getClassic() {
		return this.chassis;
	}

	@Override
	public Position2d getCurrentPose() {
		return this.RobotPosition;
	}

	/**
	 * @param driveOrderPackage 要执行的DriveCommandPackage，不建议在使用时才定义driveCommandPackage，虽然没有任何坏处
	 */
	@Override
	public void runOrderPackage(@NonNull final DriveOrderPackage driveOrderPackage){
		this.runOrderPackage(driveOrderPackage.getOrder());
	}

	/**
	 * @return 定义开启新的drivingCommandsBuilder
	 */
	public DrivingCommandsBuilder drivingCommandsBuilder(){
		return new DrivingCommandsBuilder(this);
	}

	@Override
	public void update(){
		this.localizer.update();
		this.RobotPosition = this.localizer.getCurrentPose();

		this.client.dashboard.drawRobot(this.RobotPosition, DashboardClient.Blue, "RobotPosition");

		this.poseHistory.add(this.RobotPosition);
	}
}
