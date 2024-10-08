package org.firstinspires.ftc.teamcode.codes.templates;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@Templates
public abstract class AutonomousProgramTemplate extends LinearOpMode {
	public Robot robot;
	public SimpleMecanumDrive drive;

	public void Init(Pose2d position){
		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(position);
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
