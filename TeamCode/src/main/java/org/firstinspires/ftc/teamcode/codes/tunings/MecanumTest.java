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
		lf=hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.LeftFront);
		lr=hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.LeftRear);
		rf=hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.RightFront);
		rr=hardwareMap.get(DcMotorEx.class, Params.HardwareNamespace.RightRear);
	}

	@Override
	public void whileActivating() {
		lf.setPower(-0.1f);
		lr.setPower(+0.1f);
		rf.setPower(+0.1f);
		rr.setPower(-0.1f);
	}
}
