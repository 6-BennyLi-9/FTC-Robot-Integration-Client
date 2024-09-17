package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Functions;

import java.util.Vector;

@OdometerPrograms
public class ClassicOdometer implements Odometry{
	protected Vector < Pose2d > PoseHistory;
	private final Client client;
	private String color;

	public ClassicOdometer(Client client){
		this.client=client;
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		AddDelta(Functions.Alignment2d(relDeltaX, relDeltaY, relDeltaTheta));
	}

	@Override
	public void registerLineToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer");
		for (int i = 0, poseHistorySize = PoseHistory.size(); i < poseHistorySize - 1; i++) {
			client.dashboard.DrawLine(PoseHistory.get(i),PoseHistory.get(i+1),"Odometer", color);
		}
	}

	@Override
	public void registerRobotToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer Robot");
		client.dashboard.DrawRobot(LastPose(),color,"Odometer Robot");
	}

	@Override
	public Pose2d getCurrentPose() {
		return LastPose();
	}

	@Override
	public void setColor(String color) {
		this.color=color;
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
