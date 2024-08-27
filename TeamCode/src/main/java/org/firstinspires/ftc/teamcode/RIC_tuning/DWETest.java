package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.utils.AutonomousProgramTemplate;

@TeleOp(name = "DeadWheelEncoders_Test",group = "tune")
@TuningOpModes
public class DWETest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.client.changeDate("L",robot.classic.sensors.LeftTick);
			robot.client.changeDate("M",robot.classic.sensors.MiddleTick);
			robot.client.changeDate("R",robot.classic.sensors.RightTick);
		}
	}
}
