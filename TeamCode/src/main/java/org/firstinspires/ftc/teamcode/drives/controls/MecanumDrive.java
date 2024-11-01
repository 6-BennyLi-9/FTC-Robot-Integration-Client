package org.firstinspires.ftc.teamcode.drives.controls;

import static org.firstinspires.ftc.teamcode.Params.aem;
import static org.firstinspires.ftc.teamcode.Params.pem;
import static org.firstinspires.ftc.teamcode.Params.timeOutProtectionMills;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.drives.controls.actions.DriveAction;
import org.firstinspires.ftc.teamcode.drives.controls.actions.DriveActionBuilder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.drives.localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.drives.localizers.plugins.DeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Motors;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.annotations.DrivingPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;
import org.firstinspires.ftc.teamcode.utils.enums.RobotState;

import java.util.LinkedList;

@DrivingPrograms
public class MecanumDrive implements DriverProgram {
	public final Chassis chassis;
	private final Motors motors;
	private final Client client;
	private final PidProcessor pidProcessor;
	private final String[] ContentTags;

	public final LinkedList<Position2d> poseHistory = new LinkedList<>();
	public Position2d RobotPosition;
	public double BufPower= 1.0f;

	public final Localizer localizer;

	public RobotState robotState;

	public MecanumDrive(final Position2d RobotPosition){
		client=Global.client;
		pidProcessor=Global.robot.pidProcessor;
		robotState = Robot.robotState;
		this.RobotPosition=RobotPosition;

		chassis= Global.robot.chassis;

		this.motors = Global.robot.motors;

		//TODO:更换Localizer如果需要
		this.localizer =new DeadWheelLocalizer(Global.robot.sensors);

		this.ContentTags =new String[]{"DRIVE-X", "DRIVE-Y", "DRIVE-HEADING"};

		pidProcessor.loadContent(new PidContent(this.ContentTags[0], 0));
		pidProcessor.loadContent(new PidContent(this.ContentTags[1],1));
		pidProcessor.loadContent(new PidContent(this.ContentTags[2],2));
	}

	@Override
	public void update() {
		this.localizer.update();
		this.RobotPosition = this.localizer.getCurrentPose();

		this.client.dashboard.drawRobot(this.RobotPosition, DashboardClient.Blue, "RobotPosition");

		this.poseHistory.add(this.RobotPosition);
	}

	@Override
	public void runOrderPackage(@NonNull final DriveOrderPackage orderPackage) {
		this.runOrderPackage(orderPackage.getOrder());
	}

	/**
	 * @see SimpleMecanumDrive
	 */
	@Override
	public void runOrderPackage(@NonNull final LinkedList<DriveOrder> orders) {
		final DriveAction[] commandLists =new DriveAction[orders.size()];
		for (int i = 0 ; i < orders.size(); i++) {
			commandLists[i]= (DriveAction) orders.get(i);
		}

		final Vector2d[] PoseList;
		PoseList=new Vector2d[commandLists.length+1];
		PoseList[0]=commandLists[0].pose.toVector();
		final Timer timer = new Timer();

		for(int i=0;i<commandLists.length;++i){
			PoseList[i+1]=commandLists[i].nextPose().toVector();
		}

		Actions.runBlocking(new Action() {
			public int ID;
			@Override
			public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
				final DriveAction singleAction =commandLists[this.ID];
				singleAction.run();
				MecanumDrive.this.update();
				MecanumDrive.this.motors.updateDriveOptions(MecanumDrive.this.RobotPosition.heading);

				MecanumDrive.this.BufPower = singleAction.BufPower;
				final double dY            = singleAction.getDeltaTrajectory().y;
				final double dX            = singleAction.getDeltaTrajectory().x;
				final double distance      = Math.sqrt(dX * dX + dY * dY);
				final double estimatedTime = distance/(Params.secPowerPerInch * MecanumDrive.this.BufPower);
				MecanumDrive.this.client.changeData("distance",distance);
				MecanumDrive.this.client.changeData("estimatedTime",estimatedTime);
				MecanumDrive.this.client.changeData("progress","0%");
				MecanumDrive.this.client.changeData("DELTA", singleAction.getDeltaTrajectory().toString());

				timer.restart();
				while ((Math.abs(MecanumDrive.this.RobotPosition.x - PoseList[this.ID + 1].x) > pem)
						&& (Math.abs(MecanumDrive.this.RobotPosition.y - PoseList[this.ID + 1].y) > pem)
						&& (Math.abs(MecanumDrive.this.RobotPosition.heading - singleAction.nextPose().heading) > aem)){
					final double progress = (timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
					MecanumDrive.this.client.changeData("progress", progress + "%");
					final Position2d aim = Functions.getAimPositionThroughTrajectory(singleAction, MecanumDrive.this.RobotPosition,progress);

					if(timer.getDeltaTime()>estimatedTime+ timeOutProtectionMills&& Params.Configs.useOutTimeProtection){//保护机制
						MecanumDrive.this.robotState = RobotState.BrakeDown;
						MecanumDrive.this.motors.updateDriveOptions();
						break;
					}

					if(Params.Configs.usePIDToDriveInAutonomous){
						if(Math.abs(aim.x - MecanumDrive.this.RobotPosition.x) > pem
								|| Math.abs(aim.y - MecanumDrive.this.RobotPosition.y) > pem
								|| Math.abs(aim.heading - MecanumDrive.this.RobotPosition.heading) > aem
								|| Params.Configs.alwaysRunPIDInAutonomous ){
							//间断地调用pid可能会导致pid的效果不佳
							MecanumDrive.this.pidProcessor.registerInaccuracies(MecanumDrive.this.ContentTags[0], aim.x - MecanumDrive.this.RobotPosition.x);
							MecanumDrive.this.pidProcessor.registerInaccuracies(MecanumDrive.this.ContentTags[1], aim.y - MecanumDrive.this.RobotPosition.y);
							MecanumDrive.this.pidProcessor.registerInaccuracies(MecanumDrive.this.ContentTags[2], aim.heading - MecanumDrive.this.RobotPosition.heading);

							MecanumDrive.this.pidProcessor.update();

							MecanumDrive.this.motors.xAxisPower+= MecanumDrive.this.pidProcessor.getFulfillment(MecanumDrive.this.ContentTags[0]);
							MecanumDrive.this.motors.yAxisPower+= MecanumDrive.this.pidProcessor.getFulfillment(MecanumDrive.this.ContentTags[1]);
							MecanumDrive.this.motors.headingPower+= MecanumDrive.this.pidProcessor.getFulfillment(MecanumDrive.this.ContentTags[2]);
						}
					}else{
						if(Math.abs(aim.x - MecanumDrive.this.RobotPosition.x) > pem
								|| Math.abs(aim.y - MecanumDrive.this.RobotPosition.y) > pem
								|| Math.abs(aim.heading - MecanumDrive.this.RobotPosition.heading) > aem){
							final double[] fulfillment= {
									(aim.x - MecanumDrive.this.RobotPosition.x) * (Params.secPowerPerInch) * MecanumDrive.this.BufPower / 2,
									(aim.y - MecanumDrive.this.RobotPosition.y) * (Params.secPowerPerInch) * MecanumDrive.this.BufPower / 2,
									(aim.heading > MecanumDrive.this.RobotPosition.heading ? MecanumDrive.this.BufPower / 2 : - MecanumDrive.this.BufPower / 2)
							};

							MecanumDrive.this.motors.xAxisPower+=fulfillment[0];
							MecanumDrive.this.motors.yAxisPower+=fulfillment[1];
							MecanumDrive.this.motors.headingPower+=fulfillment[2];
						}
					}

					MecanumDrive.this.motors.updateDriveOptions(MecanumDrive.this.RobotPosition.heading);
				}

				if(this.ID != commandLists.length - 1){
					++ this.ID;
					return true;
				}else{
					return false;
				}
			}
			@Override
			public void preview(@NonNull final Canvas fieldOverlay){
				fieldOverlay.setStroke(DashboardClient.Green);
				for(int i=0;i<PoseList.length;++i){
					fieldOverlay.strokeLine(PoseList[i].x,PoseList[i].y,PoseList[i+1].x,PoseList[i+1].y);
				}
			}
		});

		this.client.deleteData("distance");
		this.client.deleteData("estimatedTime");
		this.client.deleteData("progress");
		this.client.deleteData("DELTA");

		this.chassis.STOP();
		this.robotState = RobotState.IDLE;
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
	 * @return 定义开启新的 DriveActionBuilder
	 */
	public DriveActionBuilder drivingCommandsBuilder(){
		return new DriveActionBuilder(this);
	}
}
