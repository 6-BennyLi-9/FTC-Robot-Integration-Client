package org.firstinspires.ftc.teamcode.tunings;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.templates.AutonomousProgramTemplate;

@TeleOp(name = "AxialInchPerTickTest",group = "tune")
@Disabled
@Deprecated
public class AxialInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("Ticks",robot.sensors.getDeltaA());
			robot.changeData("Inch Drove",robot.sensors.getDeltaA()* Params.AxialInchPerTick);
		}
	}
}
