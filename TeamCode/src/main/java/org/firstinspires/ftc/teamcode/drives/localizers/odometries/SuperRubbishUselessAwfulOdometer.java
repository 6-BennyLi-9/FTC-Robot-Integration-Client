package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@OdometerPrograms
public class SuperRubbishUselessAwfulOdometer implements Odometry{
	public Position2d robotPose;
	public Client     client;
	private String color;

	public SuperRubbishUselessAwfulOdometer(){
		robotPose=new Position2d(0,0,0);
		this.client= Global.client;
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		robotPose=new Position2d(
				robotPose.x+ relDeltaX,
				robotPose.y+ relDeltaY,
				robotPose.heading+ Math.toRadians(relDeltaTheta)
		);
	}

	@Override
	public void registerLineToDashBoard(String tag) {
		//Cannot Draw
	}

	@Override
	public void registerRobotToDashBoard(String tag) {
		client.dashboard.DrawRobot(robotPose, color,tag);
	}

	@Override
	public Position2d getCurrentPose() {
		return robotPose;
	}

	@Override
	public void setColor(String color) {
		this.color=color;
	}
}
