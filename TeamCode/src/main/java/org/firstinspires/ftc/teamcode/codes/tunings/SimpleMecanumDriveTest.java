package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TestProgramTemplate;
import org.firstinspires.ftc.teamcode.drives.controls.commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@TeleOp(name = "SimpleMecanumDrive_Test",group = Params.Configs.TuningAndTuneOpModesGroup)
public class SimpleMecanumDriveTest extends TestProgramTemplate {
	Robot robot;
	SimpleMecanumDrive drive;
	DriveCommandPackage trajectory;

	@Override
	public void opInit() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,client);
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(new Position2d(0,0,0));

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
