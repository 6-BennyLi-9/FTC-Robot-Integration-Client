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
import org.firstinspires.ftc.teamcode.hardwares.basic.Motors;
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
	public double BufPower=1f;

	public final Localizer localizer;

	public RobotState robotState;

	public MecanumDrive(Position2d RobotPosition){
		this.client=Global.client;
		this.pidProcessor=Global.robot.pidProcessor;
		this.robotState = Robot.robotState;
		this.RobotPosition=RobotPosition;

		this.chassis= Global.robot.chassis;

		motors= Global.robot.motors;

		//TODO:更换Localizer如果需要
		localizer=new DeadWheelLocalizer(Global.robot.sensors);

		ContentTags=new String[]{"DRIVE-X","DRIVE-Y","DRIVE-HEADING"};

		this.pidProcessor.loadContent(new PidContent(ContentTags[0], 0));
		this.pidProcessor.loadContent(new PidContent(ContentTags[1],1));
		this.pidProcessor.loadContent(new PidContent(ContentTags[2],2));
	}

	@Override
	public void update() {
		localizer.update();
		RobotPosition = localizer.getCurrentPose();

		client.dashboard.DrawRobot(RobotPosition, DashboardClient.Blue, "RobotPosition");

		poseHistory.add(RobotPosition);
	}

	@Override
	public void runOrderPackage(@NonNull DriveOrderPackage orderPackage) {
		runOrderPackage(orderPackage.getOrder());
	}

	/**
	 * @see SimpleMecanumDrive
	 */
	@Override
	public void runOrderPackage(@NonNull LinkedList<DriveOrder> orders) {
		DriveAction[] commandLists=new DriveAction[orders.size()];
		for (int i = 0 ; i < orders.size(); i++) {
			commandLists[i]= (DriveAction) orders.get(i);
		}

		Vector2d[] PoseList;
		PoseList=new Vector2d[commandLists.length+1];
		PoseList[0]=commandLists[0].pose.asVector();
		Timer timer = new Timer();

		for(int i=0;i<commandLists.length;++i){
			PoseList[i+1]=commandLists[i].NEXT().asVector();
		}

		Actions.runBlocking(new Action() {
			public int ID=0;
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				DriveAction singleAction =commandLists[ID];
				singleAction.RUN();
				update();
				motors.updateDriveOptions(RobotPosition.heading);

				BufPower= singleAction.BufPower;
				double dY = singleAction.getDeltaTrajectory().y;
				double dX = singleAction.getDeltaTrajectory().x;
				final double distance=Math.sqrt(dX * dX + dY * dY);
				final double estimatedTime=distance/(Params.secPowerPerInch *BufPower);
				client.changeData("distance",distance);
				client.changeData("estimatedTime",estimatedTime);
				client.changeData("progress","0%");
				client.changeData("DELTA", singleAction.getDeltaTrajectory().toString());

				timer.restart();
				while ((Math.abs(RobotPosition.x-PoseList[ID+1].x)> pem)
						&& (Math.abs(RobotPosition.y-PoseList[ID+1].y)> pem)
						&& (Math.abs(RobotPosition.heading-singleAction.NEXT().heading)> aem)){
					double progress=(timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
					client.changeData("progress", progress +"%");
					Position2d aim= Functions.getAimPositionThroughTrajectory(singleAction,RobotPosition,progress);

					if(timer.getDeltaTime()>estimatedTime+ timeOutProtectionMills&& Params.Configs.useOutTimeProtection){//保护机制
						robotState = RobotState.BrakeDown;
						motors.updateDriveOptions();
						break;
					}

					if(Params.Configs.usePIDInAutonomous){
						if(Math.abs(aim.x- RobotPosition.x)> pem
								|| Math.abs(aim.y- RobotPosition.y)> pem
								|| Math.abs(aim.heading- RobotPosition.heading)> aem
								|| Params.Configs.alwaysRunPIDInAutonomous ){
							//间断地调用pid可能会导致pid的效果不佳
							pidProcessor.registerInaccuracies(ContentTags[0], aim.x- RobotPosition.x);
							pidProcessor.registerInaccuracies(ContentTags[1], aim.y- RobotPosition.y);
							pidProcessor.registerInaccuracies(ContentTags[2], aim.heading- RobotPosition.heading);

							pidProcessor.update();

							motors.xAxisPower+=pidProcessor.getFulfillment(ContentTags[0]);
							motors.yAxisPower+=pidProcessor.getFulfillment(ContentTags[1]);
							motors.headingPower+=pidProcessor.getFulfillment(ContentTags[2]);
						}
					}else{
						if(Math.abs(aim.x- RobotPosition.x)> pem
								|| Math.abs(aim.y- RobotPosition.y)> pem
								|| Math.abs(aim.heading- RobotPosition.heading)> aem){
							double[] fulfillment=new double[]{
									(aim.x- RobotPosition.x)*(Params.secPowerPerInch)*BufPower/2,
									(aim.y- RobotPosition.y)*(Params.secPowerPerInch)*BufPower/2,
									(aim.heading> RobotPosition.heading? BufPower/2:-BufPower/2)
							};

							motors.xAxisPower+=fulfillment[0];
							motors.yAxisPower+=fulfillment[1];
							motors.headingPower+=fulfillment[2];
						}
					}

					motors.updateDriveOptions(RobotPosition.heading);
				}

				if(ID!=commandLists.length-1){
					++ID;
					return true;
				}else{
					return false;
				}
			}
			@Override
			public void preview(@NonNull Canvas fieldOverlay){
				fieldOverlay.setStroke(DashboardClient.Green);
				for(int i=0;i<PoseList.length;++i){
					fieldOverlay.strokeLine(PoseList[i].x,PoseList[i].y,PoseList[i+1].x,PoseList[i+1].y);
				}
			}
		});

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
	 * @return 定义开启新的 DriveActionBuilder
	 */
	public DriveActionBuilder drivingCommandsBuilder(){
		return new DriveActionBuilder(this);
	}
}
