package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.Utils.Templates.AutonomousProgramTemplate;

/**
 * 1.把机器的正方向的边缘对齐放在地垫的交界处
 * <p>
 * 2.运行程序
 * <p>
 * 3.把机器移动到地垫的另一端
 * <p>
 * 4.查看xError和yError，填入{@link org.firstinspires.ftc.teamcode.Params}
 */
@TeleOp(name = "IMUPositionTuner",group = "tune")
@TuningOpModes
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
			r=robot.sensors.XMoved/2;
			xP=r;
			yP=robot.sensors.YMoved-2*r;
			robot.changeData ("xError",xP);
			robot.changeData("yError", yP);
			robot.changeData("R",r);
			robot.changeData("X", robot.sensors.XMoved);
			robot.changeData("Y", robot.sensors.YMoved);
			robot.changeData("Heading", robot.sensors.FirstAngle);
		}
	}
}
