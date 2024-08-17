package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

@Autonomous(name = "SimpleMecanumDrive_Test",group = "tune")
public class SMD_Test extends LinearOpMode {
	Robot robot;
	SimpleMecanumDrive drive;

	@Override
	public void runOpMode() {
		robot=new Robot(hardwareMap, runningState.Autonomous,new Client(telemetry));

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		drive=robot.enableSimpleMecanumDrive(new Pose2d(0,0,0));

		DriveCommandPackage trajectory=drive.drivingCommandsBuilder()
				.StrafeInDistance(0,24)
				.TurnAngle(90)
				.StrafeTo(new Vector2d(24,0))
				.END();

		drive.runDriveCommandPackage(trajectory);
		robot.update();
		robot.turnAngle(-90);

		sleep(1145141919);
	}
}
