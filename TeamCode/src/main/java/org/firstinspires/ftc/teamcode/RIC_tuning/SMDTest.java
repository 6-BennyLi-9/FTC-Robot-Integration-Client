package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.utils.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Enums.runningState;

@Autonomous(name = "SimpleMecanumDrive_Test",group = "tune")
@TuningOpModes
public class SMDTest extends AutonomousProgramTemplate {
	Robot robot;
	SimpleMecanumDrive drive;

	@Override
	public void runOpMode() {
		robot=new Robot(hardwareMap, runningState.Autonomous,telemetry);

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		drive=robot.InitMecanumDrive(new Pose2d(0,0,0));

		DriveCommandPackage trajectory=robot.drivingCommandsBuilder()
				.StrafeInDistance(0,24)
				.TurnAngle(90)
				.StrafeTo(new Vector2d(24,0))
				.END();

		drive.runCommandPackage(trajectory);
		robot.update();
		robot.turnAngle(-90);

		sleep(1145141919);
	}
}
