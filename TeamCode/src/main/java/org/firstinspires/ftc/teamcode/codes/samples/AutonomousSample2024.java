package org.firstinspires.ftc.teamcode.codes.samples;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;
import org.firstinspires.ftc.teamcode.drives.controls.commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;

/**
 * A sample of 2023-2024 season basic Autonomous.
 *
 * 基於 FTC 2023-2034 賽季的代碼樣式
 */
@Autonomous(name = "AutonomousSample",group = Params.Configs.SampleOpModesGroup)
@Disabled
//@Deprecated
public class AutonomousSample2024 extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Webcam.useWebcam=true;
		Init(new Pose2d(0,0,0));
		robot.addData("Position","WAITING FOR REQUEST");
		AutonomousLocation location = AutonomousLocation.failed;
		while (opModeIsNotActive()) {
			location=robot.webcam.getLocation();
			robot.changeData("Position",location.name());
			sleep(50);
		}

		if(WaitForStartRequest())return;

		robot.deleteData("Position");
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
				drive.runOrderPackage(command);
				break;
			case right:
				break;
			case failed:
				return;
		}

		sleep(1145141919);
	}
}
