package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.ImuLocalizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Client;
import org.firstinspires.ftc.teamcode.utils.enums.runningState;

/**
 * 1.把机器的正方向的边缘对齐放在地垫的交界处
 * <p>
 * 2.运行程序
 * <p>
 * 3.把机器移动到地垫的另一端
 * <p>
 * 4.查看xError和yError，填入{@link ImuLocalizer.Params}
 */
@TeleOp(name = "IMUPositionTuner",group = "tune")
public class IMUPositionTuner extends LinearOpMode {
	public Robot robot;

	@Override
	public void runOpMode() {
		double xP,yP,r;

		INIT();

		while(!opModeIsActive()&&!isStopRequested()){
			sleep(50);
		}

		if (!opModeIsActive() || isStopRequested())return;

		robot.client.addData("xError","waitingForFeedback");
		robot.client.addData("yError","waitingForFeedback");

		while(!isStopRequested()){
			robot.update();
			r=robot.sensors.XMoved/2;
			xP=r;
			yP=robot.sensors.YMoved-2*r;
			robot.client.changeDate("xError",String.valueOf(xP));
			robot.client.changeDate("yError", String.valueOf(yP));
		}
	}
	public void INIT(){
		robot=new Robot(hardwareMap, runningState.Autonomous,new Client(telemetry));
	}
}
