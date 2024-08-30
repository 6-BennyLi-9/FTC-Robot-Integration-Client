package org.firstinspires.ftc.teamcode.DriveControls;

import static org.firstinspires.ftc.teamcode.Params.aem;
import static org.firstinspires.ftc.teamcode.Params.pem;
import static org.firstinspires.ftc.teamcode.Params.timeOutProtectionMills;
import static org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient.Blue;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.DriveControls.Actions.DriveAction;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.DeadWheelSubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.LocalizerDefinition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrder;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriverProgram;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Annotations.DrivingPrograms;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient;
import org.firstinspires.ftc.teamcode.Utils.Enums.State;
import org.firstinspires.ftc.teamcode.Utils.Functions;
import org.firstinspires.ftc.teamcode.Utils.PID_processor;
import org.firstinspires.ftc.teamcode.Utils.Timer;

import java.util.LinkedList;

@DrivingPrograms
public class MecanumDrive implements DriverProgram {
	public final Classic classic;
	private final Motors motors;
	private final Client client;
	private final PID_processor pidProcessor;

	public final LinkedList<Pose2d> poseHistory = new LinkedList<>();
	public Pose2d RobotPosition;
	public double BufPower=1f;

	public final Localizer localizer;

	public State state;

	public MecanumDrive(@NonNull Classic classic, Client client,
	                    PID_processor pidProcessor, State state, Pose2d RobotPosition){
		this.classic=classic;
		this.client=client;
		this.pidProcessor=pidProcessor;
		this.state=state;
		this.RobotPosition=RobotPosition;
		motors=classic.motors;

		//TODO:更换Localizer如果需要
		localizer=new DeadWheelSubassemblyLocalizer(classic);
	}
	@ExtractedInterfaces
	public MecanumDrive(@NonNull Robot robot,Pose2d RobotPosition){
		this(robot.classic,robot.client,robot.pidProcessor,robot.state,RobotPosition);
	}

	@Override
	public void update() {
		localizer.update();
		RobotPosition = localizer.getCurrentPose();

		client.dashboard.DrawRobot(RobotPosition, Blue);

		poseHistory.add(RobotPosition);
	}

	@Override
	public void runCommandPackage(@NonNull DriveOrderPackage orderPackage) {
		runCommandPackage(orderPackage.getOrder());
	}

	/**
	 * @see SimpleMecanumDrive
	 */
	@Override
	public void runCommandPackage(@NonNull LinkedList<DriveOrder> orders) {
		DriveAction[] commandLists=new DriveAction[orders.size()];
		for (int i = 0 ; i < orders.size(); i++) {
			commandLists[i]= (DriveAction) orders.get(i);
		}

		Vector2d[] PoseList;
		PoseList=new Vector2d[commandLists.length+1];
		PoseList[0]=commandLists[0].pose.position;
		Timer timer = new Timer();

		for(int i=0;i<commandLists.length;++i){
			PoseList[i+1]=commandLists[i].NEXT().position;
		}

		Actions.runBlocking(new Action() {
			public int ID=0;
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				DriveAction singleAction =commandLists[ID];
				singleAction.RUN();
				update();
				motors.updateDriveOptions(RobotPosition.heading.toDouble());

				BufPower= singleAction.BufPower;
				double dY = singleAction.getDeltaTrajectory().position.y;
				double dX = singleAction.getDeltaTrajectory().position.x;
				final double distance=Math.sqrt(dX * dX + dY * dY);
				final double estimatedTime=distance/(Params.vP*BufPower);
				client.changeData("distance",distance);
				client.changeData("estimatedTime",estimatedTime);
				client.changeData("progress","0%");
				client.changeData("DELTA", singleAction.getDeltaTrajectory().toString());

				timer.restart();
				while ((Math.abs(RobotPosition.position.x-PoseList[ID+1].x)> pem)
						&& (Math.abs(RobotPosition.position.y-PoseList[ID+1].y)> pem)
						&& (Math.abs(RobotPosition.heading.toDouble()-singleAction.NEXT().heading.toDouble())> aem)){
					double progress=(timer.stopAndGetDeltaTime() / 1000.0) / estimatedTime * 100;
					client.changeData("progress", progress +"%");
					Pose2d aim= Functions.getAimPositionThroughTrajectory(singleAction,RobotPosition,progress);

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

		classic.STOP();
		state= State.IDLE;
	}

	@Override
	public Classic getClassic() {
		return classic;
	}
}