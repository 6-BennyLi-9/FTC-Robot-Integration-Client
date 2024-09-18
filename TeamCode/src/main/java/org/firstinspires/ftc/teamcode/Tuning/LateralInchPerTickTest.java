package org.firstinspires.ftc.teamcode.Tuning;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleTeleOPs;

@TuningOrSampleTeleOPs(name = "LateralInchPerTickTest",group = "tune")
public class LateralInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));
		robot.addData("Ticks","WAITING FOR REQUEST");

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("Ticks",robot.sensors.getDeltaL());
			robot.changeData("Inch Drove",robot.sensors.getDeltaA()* Params.LateralInchPerTick);
		}
	}
}
