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
		client= Global.client;

		this.PoseHistory =new Vector<>();
	}

	@Override
	public void update(final double relDeltaX, final double relDeltaY, final double relDeltaTheta) {
		this.AddUnAlignmentDelta(relDeltaX, relDeltaY, relDeltaTheta);
	}

	@Override
	public void registerLineToDashBoard() {
		for (int i = 0, poseHistorySize = this.PoseHistory.size() ; i < poseHistorySize - 1; i++) {
			this.client.dashboard.drawLine(this.PoseHistory.get(i), this.PoseHistory.get(i + 1), this.color);
		}
	}

	@Override
	public void registerRobotToDashBoard(final String tag) {
		this.client.dashboard.drawRobot(this.LastPose(), this.color,tag);
	}

	@Override
	public Position2d getCurrentPose() {
		return this.LastPose();
	}

	@Override
	public void setColor(final String color) {
		this.color=color;
	}

	protected Position2d LastPose(){
		return this.PoseHistory.isEmpty() ? new Position2d(0,0,0) : this.PoseHistory.lastElement();
	}

	protected void AddDelta(final double DeltaGlobalX, final double DeltaGlobalY, final double DeltaTheta){
		final Position2d cache = this.LastPose();
		this.PoseHistory.add(new Position2d(
				cache.x+ DeltaGlobalX,
				cache.y+ DeltaGlobalY,
				cache.heading+ Math.toRadians(DeltaTheta)
		));
	}
	protected void AddUnAlignmentDelta(final double DeltaGlobalX, final double DeltaGlobalY, final double DeltaTheta){
		final Position2d pose = Functions.Alignment2d(DeltaGlobalX,DeltaGlobalY, DeltaTheta + this.LastPose().heading);
		this.AddDelta(pose.x,pose.y,DeltaTheta);
	}
}
