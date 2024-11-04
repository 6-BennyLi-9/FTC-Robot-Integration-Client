package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;


@Autonomous(name = "MecanumTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class MecanumTest extends TuningProgramTemplate {

	DcMotorEx lf,lr,rf,rr;

	@Override
	public void whenInit() {
		this.lf = this.hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.leftFront);
		this.lr = this.hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.leftRear);
		this.rf = this.hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.rightFront);
		this.rr = this.hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.rightRear);
	}

	@Override
	public void whileActivating() {
		this.lf.setPower(-0.1f);
		this.lr.setPower(+0.1f);
		this.rf.setPower(+0.1f);
		this.rr.setPower(-0.1f);
	}
}
