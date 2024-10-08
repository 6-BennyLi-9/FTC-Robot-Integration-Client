package org.firstinspires.ftc.teamcode.codes.tunings;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;

@TeleOp(name = "LocalizationTest",group = "tune")
public class LocalizationTest extends TuningProgramTemplate {
	SimpleMecanumDrive drive;
	@Override
	public void whenInit() {
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(new Pose2d(0,0,0));
		robot.addData("POSITION","WAIT FOR START");
		robot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void whileActivating() {
		robot.update();
		drive.update();

		robot.changeData("POSITION",drive.localizer.getCurrentPose().toString());
		robot.changeData("X",drive.localizer.getCurrentPose().position.x);
		robot.changeData("Y",drive.localizer.getCurrentPose().position.y);
		robot.changeData("HEADING",Math.toDegrees(drive.localizer.getCurrentPose().heading.toDouble()));

		robot.operateThroughGamePad();
	}
}
