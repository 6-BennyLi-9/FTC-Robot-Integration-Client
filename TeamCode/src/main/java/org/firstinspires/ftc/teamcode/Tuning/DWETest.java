package org.firstinspires.ftc.teamcode.Tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Templates.AutonomousProgramTemplate;

@TeleOp(name = "DeadWheelEncoders_Test",group = "tune")
public class DWETest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("L",robot.classic.sensors.Left.encTick);
			robot.changeData("M",robot.classic.sensors.Middle.encTick);
			robot.changeData("R",robot.classic.sensors.Right.encTick);
		}
	}
}
