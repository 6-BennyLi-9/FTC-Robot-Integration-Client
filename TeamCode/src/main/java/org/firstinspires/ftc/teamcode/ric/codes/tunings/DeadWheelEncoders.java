package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;

@TeleOp(name = "DeadWheelEncoders_Test",group = Params.Configs.TuningAndTuneOpModesGroup)
@Deprecated
@Disabled
public class DeadWheelEncoders extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		robot.update();
		robot.changeData("L",robot.chassis.sensors.Left.encTick);
		robot.changeData("M",robot.chassis.sensors.Middle.encTick);
		robot.changeData("R",robot.chassis.sensors.Right.encTick);
	}

	@Override
	public void whenInit() {
	}
}
