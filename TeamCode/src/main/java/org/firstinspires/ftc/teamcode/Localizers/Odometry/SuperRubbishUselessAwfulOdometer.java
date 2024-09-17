package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient;

@OdometerPrograms
public class SuperRubbishUselessAwfulOdometer implements Odometry{
	public Pose2d robotPose;
	public Client client;

	public SuperRubbishUselessAwfulOdometer(Client client){
		robotPose=new Pose2d(0,0,0);
	}

	@Override
	public void ProcessDeltaRelPose(double relX, double relY, double relTheta) {
		robotPose=new Pose2d(
				robotPose.position.x+relX,
				robotPose.position.y+relY,
				robotPose.heading.toDouble()+relTheta
		);
	}

	@Override
	public void RegisterLineToDashBoard() {
		//Cannot Draw
	}

	@Override
	public void RegisterRobotToDashBoard() {
		client.dashboard.deletePacketByTag("Odometer Robot");
		client.dashboard.DrawRobot(robotPose, DashboardClient.Blue,"Odometer Robot");
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPose;
	}
}
