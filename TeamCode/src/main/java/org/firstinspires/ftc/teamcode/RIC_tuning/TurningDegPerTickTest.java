package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.utils.AutonomousProgramTemplate;

public class TurningDegPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));
		robot.client.addData("Ticks","WAITING FOR REQUEST");

		if(!WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.client.changeDate("Ticks",String.valueOf(robot.classic.encoders.TurningTicks));
		}
	}
}
