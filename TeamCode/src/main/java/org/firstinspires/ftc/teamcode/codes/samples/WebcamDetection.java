package org.firstinspires.ftc.teamcode.codes.samples;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;

@Disabled
@Autonomous(name = "WebcamDetection", group = Params.Configs.SampleOpModesGroup)
public class WebcamDetection extends AutonomousProgramTemplate {
	Webcam webcam=new Webcam(this.hardwareMap);

	@Override
	public void runOpMode() {
		while(! this.isStopRequested()){
			this.telemetry.addData("location", this.webcam.getLocation());
			if(this.opModeIsActive()){
				this.webcam.showRoiVP();
			}
			this.telemetry.update();
			Actions.runBlocking(new SleepAction(0.05));
		}
	}
}
