package org.firstinspires.ftc.teamcode.codes.templates;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@Templates
public abstract class TuningProgramTemplate extends TeleopProgramTemplate{
	public Client client;

	@Override
	public void init() {
		Global.clear();
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		timer=new Timer();
		client=robot.client;
		whenInit();
	}
}
