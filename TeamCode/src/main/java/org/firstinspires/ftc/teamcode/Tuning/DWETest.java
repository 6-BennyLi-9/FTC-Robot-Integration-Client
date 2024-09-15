package org.firstinspires.ftc.teamcode.Tuning;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleTeleOPs;

@TuningOrSampleTeleOPs(name = "DeadWheelEncoders_Test",group = "tune")
public class DWETest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("L",robot.classic.sensors.LeftTick);
			robot.changeData("M",robot.classic.sensors.MiddleTick);
			robot.changeData("R",robot.classic.sensors.RightTick);
		}
	}
}
