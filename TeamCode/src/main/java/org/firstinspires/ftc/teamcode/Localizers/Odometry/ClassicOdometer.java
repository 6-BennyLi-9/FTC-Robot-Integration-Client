package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient;

import java.util.Vector;

@OdometerPrograms
public class ClassicOdometer implements Odometry{
	protected Vector < Pose2d > PoseHistory;
	private final Client client;

	public ClassicOdometer(Client client){
		this.client=client;
	}

	@Override
	public void ProcessDeltaRelPose(double relX, double relY, double relTheta) {
		AddDelta(
				relX*Math.cos(Math.toRadians(relTheta))-relY*Math.sin(Math.toRadians(relTheta)),
				relY*Math.cos(Math.toRadians(relTheta))+relX*Math.sin(Math.toRadians(relTheta)),
				relTheta
		);
	}

	@Override
	public void RegisterLineToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer");
		for (int i = 0, poseHistorySize = PoseHistory.size(); i < poseHistorySize - 1; i++) {
			client.dashboard.DrawLine(PoseHistory.get(i),PoseHistory.get(i+1),"Odometer", DashboardClient.Blue);
		}
	}

	@Override
	public void RegisterRobotToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer Robot");
		client.dashboard.DrawRobot(PoseHistory.lastElement(),DashboardClient.Blue,"Odometer Robot");
	}

	@Override
	public Pose2d getCurrentPose() {
		return PoseHistory.lastElement();
	}

	protected Pose2d LastPose(){
		return PoseHistory.lastElement();
	}
	protected void AddDelta(double DeltaGlobalX, double DeltaGlobalY, double DeltaGlobalTheta){
		Pose2d cache=LastPose();
		PoseHistory.add(new Pose2d(
				cache.position.x+ DeltaGlobalX,
				cache.position.y+ DeltaGlobalY,
				cache.heading.toDouble()+ DeltaGlobalTheta
		));
	}
}
