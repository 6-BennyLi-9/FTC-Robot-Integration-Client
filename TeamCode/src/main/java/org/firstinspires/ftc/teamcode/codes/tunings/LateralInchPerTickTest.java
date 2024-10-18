package org.firstinspires.ftc.teamcode.codes.tunings;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;

@TeleOp(name = "LateralInchPerTickTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
@Deprecated
public class LateralInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("Ticks",robot.sensors.getDeltaL());
			robot.changeData("Inch Drove",robot.sensors.getDeltaL()* Params.LateralInchPerTick);
		}
	}
}
