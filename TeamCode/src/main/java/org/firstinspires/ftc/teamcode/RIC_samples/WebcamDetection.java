package org.firstinspires.ftc.teamcode.RIC_samples;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardwares.Webcam;

@Disabled
@Autonomous (name = "WebcamDetection",group = "sample")
public class WebcamDetection extends LinearOpMode {
	Webcam webcam=new Webcam();

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
