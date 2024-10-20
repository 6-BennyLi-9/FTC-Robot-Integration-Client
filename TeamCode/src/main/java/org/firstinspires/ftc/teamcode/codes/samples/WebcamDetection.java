package org.firstinspires.ftc.teamcode.codes.samples;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.Webcam;

@Disabled
@Autonomous (name = "WebcamDetection",group = Params.Configs.SampleOpModesGroup)
public class WebcamDetection extends AutonomousProgramTemplate {
	Webcam webcam=new Webcam(hardwareMap);

	@Override
	public void runOpMode() {
		while(!isStopRequested()){
			telemetry.addData("location",webcam.getLocation());
			if(opModeIsActive()){
				webcam.showRoiVP();
			}
			telemetry.update();
			Actions.runBlocking(new SleepAction(0.05));
		}
	}
}
