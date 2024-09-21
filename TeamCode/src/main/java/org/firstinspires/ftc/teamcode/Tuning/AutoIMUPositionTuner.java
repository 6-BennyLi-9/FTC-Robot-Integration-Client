package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.DriveDirection;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

/**
 * 1.运行程序
 * <p>
 * 2.查看xError和yError，填入{@link org.firstinspires.ftc.teamcode.Params}
 */
@Autonomous(name = "AutoIMUPositionTuner",group = "tune")
@Disabled
@Deprecated
public class AutoIMUPositionTuner extends AutonomousProgramTemplate {
	public Robot robot;

	@Override
	public void runOpMode() {
		double xP,yP,r;
		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		robot.turnAngle(180);
		robot.classic.drive(DriveDirection.back,0.6f);

		sleep(5000);

		robot.classic.STOP();
		robot.update();
		r=robot.sensors.getDeltaL()/2;
		xP=r;
		yP=robot.sensors.getDeltaA()-2*r;
		robot.changeData("xError",xP);
		robot.changeData("yError", yP);

		sleep(1145141919);
	}
}
