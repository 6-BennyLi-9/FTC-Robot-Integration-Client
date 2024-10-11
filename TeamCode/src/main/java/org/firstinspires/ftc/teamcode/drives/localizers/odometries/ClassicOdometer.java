package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.Functions;

import java.util.Vector;

@OdometerPrograms
public class ClassicOdometer implements Odometry{
	protected Vector < Pose2d > PoseHistory;
	private final Client client;
	private String color;

	public ClassicOdometer(Client client){
		this.client=client;

		PoseHistory=new Vector<>();
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		AddUnAlignmentDelta(relDeltaX, relDeltaY, relDeltaTheta);
	}

	@Override
	public void registerLineToDashBoard(String tag) {
		client.dashboard.deletePacketByTag(tag);
		for (int i = 0, poseHistorySize = PoseHistory.size(); i < poseHistorySize - 1; i++) {
			client.dashboard.DrawLine(PoseHistory.get(i),PoseHistory.get(i+1),tag, color);
		}
	}

	@Override
	public void registerRobotToDashBoard(String tag) {
		client.dashboard.deletePacketByTag(tag);
		client.dashboard.DrawRobot(LastPose(),color,tag);
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
		return PoseHistory.isEmpty()? new Pose2d(0,0,0) : PoseHistory.lastElement();
	}

	protected void AddDelta(double DeltaGlobalX, double DeltaGlobalY, double DeltaTheta){
		Pose2d cache=LastPose();
		PoseHistory.add(new Pose2d(
				cache.position.x+ DeltaGlobalX,
				cache.position.y+ DeltaGlobalY,
				cache.heading.toDouble()+ Math.toRadians(DeltaTheta)
		));
	}
	protected void AddUnAlignmentDelta(double DeltaGlobalX, double DeltaGlobalY, double DeltaTheta){
		Pose2d pose=Functions.Alignment2d(DeltaGlobalX,DeltaGlobalY,DeltaTheta+LastPose().heading.toDouble());
		AddDelta(pose.position.x,pose.position.y,DeltaTheta);
	}
}
