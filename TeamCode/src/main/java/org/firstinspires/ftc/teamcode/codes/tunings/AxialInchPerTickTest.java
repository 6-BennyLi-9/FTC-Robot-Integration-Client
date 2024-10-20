package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Position2d;

@TeleOp(name = "AxialInchPerTickTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
@Deprecated
public class AxialInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Position2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("Ticks",robot.sensors.getDeltaA());
			robot.changeData("Inch Drove",robot.sensors.getDeltaA()* Params.AxialInchPerTick);
		}
	}
}
