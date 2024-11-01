package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;

@TeleOp(name = "[3 in one!]DeadWheelTuner",group = Params.Configs.TuningAndTuneOpModesGroup)
public class ThreeInOne_DeadWheelTuner extends TuningProgramTemplate {
	@Override
	public void whenInit() {
		this.robot.addLine("该调参程序不具备定位和直接数据处理能力，以下所能看到的辅助调参数据都是直接相乘得到的，因此不具备定位能力！！！");

		this.robot.changeData("TurningTick","wait for start");
		this.robot.changeData("AxialTick","wait for start");
		this.robot.changeData("LateralTick","wait for start");

		this.robot.addLine("如果你已经填入了Params数据，则在这里将会直接得到相乘结果");

		this.robot.addData("TurningDeg","wait for start");
		this.robot.addData("AxialInch","wait for start");
		this.robot.addData("LateralInch","wait for start");
	}

	private double turn,axial,lateral;

	@Override
	public void whileActivating() {
		this.robot.changeData("TurningTick", this.turn += this.robot.sensors.getDeltaT());
		this.robot.changeData("AxialTick", this.axial += this.robot.sensors.getDeltaA());
		this.robot.changeData("LateralTick", this.lateral += this.robot.sensors.getDeltaL());

		this.robot.changeData("TurningDeg", this.turn * Params.TurningDegPerTick);
		this.robot.changeData("AxialInch", this.axial * Params.AxialInchPerTick);
		this.robot.changeData("LateralInch", this.lateral * Params.LateralInchPerTick);

		this.robot.sensors.updateEncoders();
	}
}
