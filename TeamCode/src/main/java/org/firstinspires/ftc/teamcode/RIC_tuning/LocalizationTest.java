package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Annotation.TuningOpModes;
import org.firstinspires.ftc.teamcode.utils.TeleopProgramTemplate;

@TeleOp(name = "LocalizationTest",group = "tune")
@TuningOpModes
public class LocalizationTest extends TeleopProgramTemplate {
	SimpleMecanumDrive drive;
	@Override
	public void whenInit() {
		drive=robot.enableSimpleMecanumDrive(new Pose2d(0,0,0));
		robot.client.addData("POSITION","WAIT FOR START");
	}

	@Override
	public void whileActivating() {
		robot.update();
		drive.update();
		robot.client.changeDate("POSITION",drive.localizer.getCurrentPose().toString());
		robot.client.changeDate("X",drive.localizer.getCurrentPose().position.x);
		robot.client.changeDate("Y",drive.localizer.getCurrentPose().position.y);
		robot.client.changeDate("HEADING",Math.toDegrees(drive.localizer.getCurrentPose().heading.toDouble()));

		robot.operateThroughGamePad(gamepad1,gamepad2);
	}
}
