package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;

/**
 * @see ChassisTest
 */
@TeleOp(name = "ChassisTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class ChassisTest extends SecPowerPerInchTuner {
	@Override
	protected double getAxialBufPower() {
		return 0.1;
	}

	@Override
	protected double getLateralBufPower() {
		return 0.1;
	}
}
