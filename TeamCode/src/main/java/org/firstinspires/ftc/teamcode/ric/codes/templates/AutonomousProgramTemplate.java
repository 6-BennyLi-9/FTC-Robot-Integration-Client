package org.firstinspires.ftc.teamcode.ric.codes.templates;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ric.Global;
import org.firstinspires.ftc.teamcode.ric.codes.samples.AutonomousSample2024;
import org.firstinspires.ftc.teamcode.ric.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.ric.Robot;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.ric.utils.enums.RunningMode;


/**
 * @see AutonomousSample2024
 */
@Templates
public abstract class AutonomousProgramTemplate extends LinearOpMode {
	public Robot robot;
	public SimpleMecanumDrive drive;

	public void Init(Pose2d position){
		Global.clear();

		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(position);

		FtcDashboard.getInstance().sendTelemetryPacket(new TelemetryPacket(true));
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
