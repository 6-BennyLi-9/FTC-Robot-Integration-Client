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
			robot.client.changeDate("AxialTicks",robot.classic.encoders.AxialTicks);
			robot.client.changeDate("LateralTicks",robot.classic.encoders.LateralTicks);
			robot.client.changeDate("TurningTicks",robot.classic.encoders.TurningTicks);

			robot.client.changeDate("L",robot.classic.encoders.Left.getPositionAndVelocity().position);
			robot.client.changeDate("M",robot.classic.encoders.Middle.getPositionAndVelocity().position);
			robot.client.changeDate("R",robot.classic.encoders.Right.getPositionAndVelocity().position);
		}
	}
}
