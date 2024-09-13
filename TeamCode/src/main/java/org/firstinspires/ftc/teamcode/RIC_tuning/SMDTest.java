package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.RIC_samples.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleTeleOPs;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningState;

@TuningOrSampleTeleOPs(name = "SimpleMecanumDrive_Test",group = "tune")
public class SMDTest extends AutonomousProgramTemplate {
	Robot robot;
	SimpleMecanumDrive drive;

	@Override
	public void runOpMode() {
		robot=new Robot(hardwareMap, RunningState.Autonomous,telemetry);

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(new Pose2d(0,0,0));

		DriveCommandPackage trajectory= (DriveCommandPackage) robot.DrivingOrderBuilder()
				.StrafeInDistance(0,24)
				.TurnAngle(90)
				.StrafeTo(new Vector2d(24,0))
				.END();

		drive.runOrderPackage(trajectory);
		robot.update();
		robot.turnAngle(-90);

		sleep(1145141919);
	}
}
