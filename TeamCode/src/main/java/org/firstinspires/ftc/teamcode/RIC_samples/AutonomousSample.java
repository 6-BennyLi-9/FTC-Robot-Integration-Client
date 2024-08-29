package org.firstinspires.ftc.teamcode.RIC_samples;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.Utils.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.AutonomousLocation;

@Autonomous(name = "AutonomousSample",group = "SAMPLE")
public class AutonomousSample extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));
		robot.client.addData("Position","WAITING FOR REQUEST");
		AutonomousLocation location = AutonomousLocation.failed;
		while (opModeIsNotActive()) {
			location=robot.webcam.getLocation();
			robot.changeData("Position",location.name());
			sleep(50);
		}

		if(WaitForStartRequest())return;

		robot.client.deleteData("Position");
		switch (location){
			case left:
				robot.strafeTo(robot.pose().position.plus(new Vector2d(0,24)));
				robot.turnAngle(90);
				robot.strafeTo(new Vector2d(24,0));
				break;
			case centre:
				DriveCommandPackage command=drive.drivingCommandsBuilder()
						.StrafeTo(robot.pose().position.plus(new Vector2d(0,24)))
						.TurnAngle(90)
						.StrafeInDistance(Math.toRadians(-90),Math.sqrt(1152))
						.END();
				drive.runCommandPackage(command);
				break;
			case right:
				break;
			case failed:
				return;
		}

		sleep(1145141919);
	}
}
