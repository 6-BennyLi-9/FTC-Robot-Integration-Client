package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.RIC_samples.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Enums.DriveDirection;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningState;

/**
 * 1.运行程序
 * <p>
 * 2.查看xError和yError，填入{@link org.firstinspires.ftc.teamcode.Params}
 */
@Autonomous(name = "AutoIMUPositionTuner",group = "tune")
@TuningOpModes
public class AutoIMUPositionTuner extends AutonomousProgramTemplate {
	public Robot robot;

	@Override
	public void runOpMode() {
		double xP,yP,r;
		robot=new Robot(hardwareMap, RunningState.Autonomous,telemetry);

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		robot.turnAngle(180);
		robot.classic.drive(DriveDirection.back,0.6f);

		sleep(5000);

		robot.classic.STOP();
		robot.update();
		r=robot.sensors.XMoved/2;
		xP=r;
		yP=robot.sensors.YMoved-2*r;
		robot.changeData("xError",xP);
		robot.changeData("yError", yP);

		sleep(1145141919);
	}
}
