package org.firstinspires.ftc.teamcode.RIC_tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.Annotation.TuningOpModes;
import org.firstinspires.ftc.teamcode.utils.AutonomousProgramTemplate;

@TeleOp(name = "AxialInchPerTickTest",group = "tune")
@TuningOpModes
public class AxialInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));
		robot.client.addData("Ticks","WAITING FOR REQUEST");

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.client.changeDate("Ticks",robot.classic.encoders.AxialTicks);
			robot.client.changeDate("Pose",robot.classic.encoders.AxialTicks* Params.AxialInchPerTick);
		}
	}
}
