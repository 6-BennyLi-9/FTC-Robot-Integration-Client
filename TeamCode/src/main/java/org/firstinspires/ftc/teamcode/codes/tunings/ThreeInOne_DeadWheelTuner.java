package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;

@TeleOp(name = "[3 in one!]DeadWheelTuner",group = Params.Configs.TuningAndTuneOpModesGroup)
public class ThreeInOne_DeadWheelTuner extends TuningProgramTemplate {
	@Override
	public void whenInit() {
		robot.addLine("该调参程序不具备定位和直接数据处理能力，以下所能看到的辅助调参数据都是直接相乘得到的，因此不具备定位能力！！！");

		robot.changeData("TurningTick","wait for start");
		robot.changeData("AxialTick","wait for start");
		robot.changeData("LateralTick","wait for start");

		robot.addLine("如果你已经填入了Params数据，则在这里将会直接得到相乘结果");

		robot.addData("TurningDeg","wait for start");
		robot.addData("AxialInch","wait for start");
		robot.addData("LateralInch","wait for start");
	}

	private double turn=0,axial=0,lateral=0;

	@Override
	public void whileActivating() {
		robot.changeData("TurningTick",turn+=robot.sensors.getDeltaT());
		robot.changeData("AxialTick",axial+=robot.sensors.getDeltaA());
		robot.changeData("LateralTick",lateral+=robot.sensors.getDeltaL());

		robot.changeData("TurningDeg",turn * Params.TurningDegPerTick);
		robot.changeData("AxialInch",axial * Params.AxialInchPerTick);
		robot.changeData("LateralInch",lateral * Params.LateralInchPerTick);

		robot.sensors.updateEncoders();
	}
}
