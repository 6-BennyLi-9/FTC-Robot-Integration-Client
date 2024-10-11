package org.firstinspires.ftc.teamcode.codes.tunings;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Params;

/**
 * 1.把机器的正方向的边缘对齐放在地垫的交界处
 * <p>
 * 2.运行程序
 * <p>
 * 3.把机器移动到地垫的另一端
 * <p>
 * 4.查看xError和yError，填入{@link Params}
 */
@TeleOp(name = "IMUPositionTuner",group = "tune")
@Disabled
@Deprecated
public class IMUPositionTuner extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		double xP,yP,r;

		Init(new Pose2d(0,0,0));

		if (WaitForStartRequest())return;

		robot.addData("xError","waitingForFeedback");
		robot.addData("yError","waitingForFeedback");

		while(!isStopRequested()){
			robot.update();
			r=robot.sensors.getDeltaL()/2;
			xP=r;
			yP=robot.sensors.getDeltaA()-2*r;
			robot.changeData ("xError",xP);
			robot.changeData("yError", yP);
			robot.changeData("R",r);
			robot.changeData("X", robot.sensors.getDeltaL());
			robot.changeData("Y", robot.sensors.getDeltaA());
			robot.changeData("Heading", robot.sensors.getDeltaT());
		}
	}
}
