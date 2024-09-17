package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

@OdometerPrograms
public class SuperRubbishUselessAwfulOdometer implements Odometry{
	public Pose2d robotPose;
	public Client client;
	private String color;

	public SuperRubbishUselessAwfulOdometer(Client client){
		robotPose=new Pose2d(0,0,0);
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		robotPose=new Pose2d(
				robotPose.position.x+ relDeltaX,
				robotPose.position.y+ relDeltaY,
				robotPose.heading.toDouble()+ relDeltaTheta
		);
	}

	@Override
	public void registerLineToDashBoard() {
		//Cannot Draw
	}

	@Override
	public void registerRobotToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer Robot");
		client.dashboard.DrawRobot(robotPose, color,"Odometer Robot");
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
