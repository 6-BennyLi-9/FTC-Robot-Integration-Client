package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

import java.util.Vector;

@OdometerPrograms
public class ClassicOdometer implements Odometry{
	protected Vector < Position2d > PoseHistory;
	private final Client client;
	private String color;

	public ClassicOdometer(){
		this.client= Global.client;

		PoseHistory=new Vector<>();
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		AddUnAlignmentDelta(relDeltaX, relDeltaY, relDeltaTheta);
	}

	@Override
	public void registerLineToDashBoard(String tag) {
		for (int i = 0, poseHistorySize = PoseHistory.size(); i < poseHistorySize - 1; i++) {
			client.dashboard.drawLine(PoseHistory.get(i),PoseHistory.get(i + 1), color);
		}
	}

	@Override
	public void registerRobotToDashBoard(String tag) {
		client.dashboard.drawRobot(LastPose(),color,tag);
	}

	@Override
	public Position2d getCurrentPose() {
		return LastPose();
	}

	@Override
	public void setColor(String color) {
		this.color=color;
	}

	protected Position2d LastPose(){
		return PoseHistory.isEmpty()? new Position2d(0,0,0) : PoseHistory.lastElement();
	}

	protected void AddDelta(double DeltaGlobalX, double DeltaGlobalY, double DeltaTheta){
		Position2d cache=LastPose();
		PoseHistory.add(new Position2d(
				cache.x+ DeltaGlobalX,
				cache.y+ DeltaGlobalY,
				cache.heading+ Math.toRadians(DeltaTheta)
		));
	}
	protected void AddUnAlignmentDelta(double DeltaGlobalX, double DeltaGlobalY, double DeltaTheta){
		Position2d pose= Functions.Alignment2d(DeltaGlobalX,DeltaGlobalY,DeltaTheta+LastPose().heading);
		AddDelta(pose.x,pose.y,DeltaTheta);
	}
}
