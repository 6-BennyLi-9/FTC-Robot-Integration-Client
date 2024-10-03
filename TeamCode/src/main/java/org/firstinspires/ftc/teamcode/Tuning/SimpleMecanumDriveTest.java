package org.firstinspires.ftc.teamcode.Tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Templates.TestProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

@TeleOp(name = "SimpleMecanumDrive_Test",group = "tune")
public class SimpleMecanumDriveTest extends TestProgramTemplate {
	Robot robot;
	SimpleMecanumDrive drive;
	DriveCommandPackage trajectory;

	@Override
	public void opInit() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,client);
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(new Pose2d(0,0,0));

		trajectory= (DriveCommandPackage) robot.DrivingOrderBuilder()
				.StrafeInDistance(0,24)
				.TurnAngle(90)
				.StrafeTo(new Vector2d(24,0))
				.END();
	}

	@Override
	public void mainCode() {
		drive.runOrderPackage(trajectory);
		robot.update();
		robot.turnAngle(-90);
	}
}
