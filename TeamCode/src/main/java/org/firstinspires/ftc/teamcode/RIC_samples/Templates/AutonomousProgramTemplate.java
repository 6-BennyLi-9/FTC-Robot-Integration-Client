package org.firstinspires.ftc.teamcode.RIC_samples.Templates;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveControls.SimpleMecanumDrive;
import core.Robot;
import org.firstinspires.ftc.teamcode.Utils.Enums.runningState;

public abstract class AutonomousProgramTemplate extends LinearOpMode {
	public Robot robot;
	public SimpleMecanumDrive drive;

	public void Init(Pose2d position){
		robot=new Robot(hardwareMap, runningState.Autonomous,telemetry);
		drive=robot.InitMecanumDrive(position);
	}

	/**
	 * @return 如果程序被停止，则返回true
	 */
	public boolean WaitForStartRequest(){
		while(opModeIsNotActive()){
			sleep(500);
		}

		return opModeStopped();
	}
	public boolean opModeIsNotActive(){
		return !opModeIsActive()&&!isStopRequested();
	}
	public boolean opModeStopped(){
		return !opModeIsActive() || isStopRequested();
	}
}
