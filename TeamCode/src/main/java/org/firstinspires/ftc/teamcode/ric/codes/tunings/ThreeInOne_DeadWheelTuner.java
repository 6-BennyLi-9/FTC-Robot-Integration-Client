package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;

/**
 * @see AxialInchPerTickTest
 * @see LateralInchPerTickTest
 * @see TurningDegPerTickTest
 * */
@TeleOp(name = "[3 in one!]DeadWheelTuner")
public class ThreeInOne_DeadWheelTuner extends TuningProgramTemplate {
	@Override
	public void whenInit() {
		robot.addLine("该调参程序不具备定位和直接数据处理能力，以下所能看到的辅助调参数据都是直接相乘得到的，因此不具备定位能力！！！");

		robot.addData("TurningDegPerTick","wait for start");
		robot.addData("AxialInchPerTick","wait for start");
		robot.addData("LateralInchPerTick","wait for start");

		robot.addLine("如果你已经填入了Params数据，则在这里将会直接得到相乘结果");

		robot.addData("TurningDegPerTick","wait for start");
		robot.addData("AxialInchPerTick","wait for start");
		robot.addData("LateralInchPerTick","wait for start");
	}

	@Override
	public void whileActivating() {
		robot.changeData("TurningDegPerTick",robot.sensors.getDeltaT());
		robot.changeData("AxialInchPerTick",robot.sensors.getDeltaA());
		robot.changeData("LateralInchPerTick",robot.sensors.getDeltaL());

		robot.changeData("TurningDegPerTick",robot.sensors.getDeltaT() * Params.TurningDegPerTick);
		robot.changeData("AxialInchPerTick",robot.sensors.getDeltaA() * Params.AxialInchPerTick);
		robot.changeData("LateralInchPerTick",robot.sensors.getDeltaL() * Params.LateralInchPerTick);
	}
}
