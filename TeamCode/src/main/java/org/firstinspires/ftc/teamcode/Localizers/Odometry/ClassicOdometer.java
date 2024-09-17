package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient;
import org.firstinspires.ftc.teamcode.Utils.Functions;

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
		AddDelta(Functions.Alignment2d(relX,relY,relTheta));
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
		client.dashboard.DrawRobot(LastPose(),DashboardClient.Blue,"Odometer Robot");
	}

	@Override
	public Pose2d getCurrentPose() {
		return LastPose();
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
	protected void AddDelta(@NonNull Pose2d delta){
		AddDelta(delta.position.x,delta.position.y,delta.heading.toDouble());
	}
}
