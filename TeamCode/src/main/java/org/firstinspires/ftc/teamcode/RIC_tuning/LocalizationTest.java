package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.utils.Templates.TeleopProgramTemplate;

@TeleOp(name = "LocalizationTest",group = "tune")
@TuningOpModes
public class LocalizationTest extends TeleopProgramTemplate {
	SimpleMecanumDrive drive;
	@Override
	public void whenInit() {
		drive=robot.InitMecanumDrive(new Pose2d(0,0,0));
		robot.addData("POSITION","WAIT FOR START");
	}

	@Override
	public void whileActivating() {
		robot.update();
		drive.update();
		robot.changeData("POSITION",drive.localizer.getCurrentPose().toString());
		robot.changeData("X",drive.localizer.getCurrentPose().position.x);
		robot.changeData("Y",drive.localizer.getCurrentPose().position.y);
		robot.changeData("HEADING",Math.toDegrees(drive.localizer.getCurrentPose().heading.toDouble()));

		robot.operateThroughGamePad(gamepad1,gamepad2);
	}
}
