package org.firstinspires.ftc.teamcode.codes.templates;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.codes.samples.AutonomousSample2024;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;


/**
 * @see AutonomousSample2024
 */
@Templates
public abstract class AutonomousProgramTemplate extends LinearOpMode {
	public Robot robot;
	public SimpleMecanumDrive drive;

	public void Init(final Position2d position){
		Global.clear();

		this.robot =new Robot(this.hardwareMap, RunningMode.Autonomous, this.telemetry);
		this.drive = (SimpleMecanumDrive) this.robot.InitMecanumDrive(position);

		FtcDashboard.getInstance().sendTelemetryPacket(new TelemetryPacket(true));
	}

	/**
	 * @return 如果程序被停止，则返回true
	 */
	public boolean WaitForStartRequest(){
		while(this.opModeIsNotActive()){
			this.sleep(500);
		}

		return this.opModeStopped();
	}
	public boolean opModeIsNotActive(){
		return ! this.opModeIsActive() && ! this.isStopRequested();
	}
	public boolean opModeStopped(){
		return ! this.opModeIsActive() || this.isStopRequested();
	}
}
