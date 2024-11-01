package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.drives.controls.commands.DriveCommandPackage;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;

/**
 * A sample of 2023-2024 season basic Autonomous.
 * <p>
 * 基於 FTC 2023-2034 賽季的代碼樣式
 */
@Autonomous(name = "AutonomousSample",group = Params.Configs.SampleOpModesGroup)
@Disabled
//@Deprecated
public class AutonomousSample2024 extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Webcam.useWebcam=true;
		this.Init(new Position2d(0,0,0));
		this.robot.addData("Position","WAITING FOR REQUEST");
		AutonomousLocation location = AutonomousLocation.failed;
		while (this.opModeIsNotActive()) {
			location= this.robot.webcam.getLocation();
			this.robot.changeData("Position",location.name());
			this.sleep(50);
		}

		if(this.WaitForStartRequest())return;

		this.robot.deleteData("Position");
		switch (location){
			case left:
				this.robot.strafeTo(this.robot.pose().plus(new Vector2d(0,24)));
				this.robot.turnAngle(90);
				this.robot.strafeTo(new Vector2d(24,0));
				break;
			case centre:
				final DriveCommandPackage command= this.drive.drivingCommandsBuilder()
						.StrafeTo(this.robot.pose().plus(new Vector2d(0,24)))
						.TurnAngle(90)
						.StrafeInDistance(- 1.5707963267948966, 33.94112549695428)
						.END();
				this.drive.runOrderPackage(command);
				break;
			case right:
				break;
			case failed:
				return;
		}

		this.sleep(1145141919);
	}
}
