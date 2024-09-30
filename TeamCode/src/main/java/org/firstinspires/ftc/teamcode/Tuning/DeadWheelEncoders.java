package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Templates.TuningProgramTemplate;

@TeleOp(name = "DeadWheelEncoders_Test",group = "tune")
@Deprecated
@Disabled
public class DeadWheelEncoders extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		robot.update();
		robot.changeData("L",robot.classic.sensors.Left.encTick);
		robot.changeData("M",robot.classic.sensors.Middle.encTick);
		robot.changeData("R",robot.classic.sensors.Right.encTick);
	}

	@Override
	public void whenInit() {
	}
}
