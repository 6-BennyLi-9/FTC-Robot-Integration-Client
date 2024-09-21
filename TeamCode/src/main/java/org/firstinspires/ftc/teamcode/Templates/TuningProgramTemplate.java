package org.firstinspires.ftc.teamcode.Templates;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;
import org.firstinspires.ftc.teamcode.Utils.Timer;

public abstract class TuningProgramTemplate extends TeleopProgramTemplate{
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		timer=new Timer();
		whenInit();
	}
}
