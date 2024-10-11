package org.firstinspires.ftc.teamcode.ric.codes.templates;

import org.firstinspires.ftc.teamcode.ric.Robot;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.ric.utils.enums.RunningMode;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

@Templates
public abstract class TuningProgramTemplate extends TeleopProgramTemplate{
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		timer=new Timer();
		whenInit();
	}
}
