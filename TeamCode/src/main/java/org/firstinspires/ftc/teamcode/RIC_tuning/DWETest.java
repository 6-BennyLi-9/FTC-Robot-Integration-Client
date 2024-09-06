package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOpModes;
import org.firstinspires.ftc.teamcode.RIC_samples.Templates.AutonomousProgramTemplate;

@TeleOp(name = "DeadWheelEncoders_Test",group = "tune")
@TuningOpModes
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
