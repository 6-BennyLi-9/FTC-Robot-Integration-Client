package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.enums.driveDirection;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

/**
 * 1.运行程序
 * <p>
 * 2.查看xError和yError，填入{@link org.firstinspires.ftc.teamcode.DriveControlsAddition.ImuLocalizer.Params}
 */
@Autonomous(name = "IMUPositionTuner",group = "tune")
public class Auto_IMUPositionTuner extends LinearOpMode {
	public Robot robot;

	@Override
	public void runOpMode() {
		double xP,yP,r;
		robot=new Robot(hardwareMap, runningState.Autonomous,new Client(telemetry));

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		robot.turnAngle(180);
		robot.classic.drive(driveDirection.back,0.6f);

		sleep(5000);

		robot.classic.STOP();
		robot.update();
		r=robot.sensors.XMoved/2;
		xP=r;
		yP=robot.sensors.YMoved-2*r;
		robot.client.changeDate("xError",String.valueOf(xP));
		robot.client.changeDate("yError", String.valueOf(yP));

		sleep(1145141919);
	}
}
