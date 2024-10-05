package org.firstinspires.ftc.teamcode.localizers.odometries;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@OdometerPrograms
public class SuperRubbishUselessAwfulOdometer implements Odometry{
	public Pose2d robotPose;
	public Client client;
	private String color;

	public SuperRubbishUselessAwfulOdometer(Client client){
		robotPose=new Pose2d(0,0,0);
		this.client=client;
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		robotPose=new Pose2d(
				robotPose.position.x+ relDeltaX,
				robotPose.position.y+ relDeltaY,
				robotPose.heading.toDouble()+ Math.toRadians(relDeltaTheta)
		);
	}

	@Override
	public void registerLineToDashBoard(String tag) {
		//Cannot Draw
	}

	@Override
	public void registerRobotToDashBoard(String tag) {
		client.dashboard.deletePacketByTag(tag);
		client.dashboard.DrawRobot(robotPose, color,tag);
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPose;
	}

	@Override
	public void setColor(String color) {
		this.color=color;
	}
}
