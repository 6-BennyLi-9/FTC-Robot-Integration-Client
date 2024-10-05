package org.firstinspires.ftc.teamcode.templates;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Templates
public abstract class TuningProgramTemplate extends TeleopProgramTemplate{
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		timer=new Timer();
		whenInit();
	}
}
