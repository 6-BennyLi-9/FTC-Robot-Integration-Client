package org.firstinspires.ftc.teamcode.Tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

@TeleOp(name = "SimpleMecanumDrive_Test",group = "tune")
public class SimpleMecanumDrive extends AutonomousProgramTemplate {
	Robot robot;
	org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive drive;

	@Override
	public void runOpMode() {
		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		drive= (org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive) robot.InitMecanumDrive(new Pose2d(0,0,0));

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
