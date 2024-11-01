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
		this.robotPose =new Position2d(0,0,0);
		client= Global.client;
	}

	@Override
	public void update(final double relDeltaX, final double relDeltaY, final double relDeltaTheta) {
		this.robotPose =new Position2d(this.robotPose.x + relDeltaX, this.robotPose.y + relDeltaY, this.robotPose.heading + Math.toRadians(relDeltaTheta)
		);
	}

	@Override
	public void registerLineToDashBoard() {
		//Cannot Draw
	}

	@Override
	public void registerRobotToDashBoard(final String tag) {
		this.client.dashboard.drawRobot(this.robotPose, this.color,tag);
	}

	@Override
	public Position2d getCurrentPose() {
		return this.robotPose;
	}

	@Override
	public void setColor(final String color) {
		this.color=color;
	}
}
