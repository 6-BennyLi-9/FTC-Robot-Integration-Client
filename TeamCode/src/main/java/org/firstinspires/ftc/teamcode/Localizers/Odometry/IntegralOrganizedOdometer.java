package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Functions;

@OdometerPrograms
public class IntegralOrganizedOdometer extends ClassicOdometer implements Odometry{
	public double distanceTraveled=0;

	public IntegralOrganizedOdometer(Client client) {
		super(client);
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		distanceTraveled += Functions.distance(relDeltaX, relDeltaY);

	}

	@Override
	public void registerLineToDashBoard() {

	}

	@Override
	public void registerRobotToDashBoard() {

	}

	@Override
	public Pose2d getCurrentPose() {
		return null;
	}
}
