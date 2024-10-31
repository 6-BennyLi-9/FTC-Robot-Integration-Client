package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Autonomous(name = "DashboardTelemetryTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class DashboardTelemetryTest extends TuningProgramTemplate {
	@Override
	public void whenInit() {

	}

	@Override
	public void whileActivating() {
		client.dashboard.put("time", Timer.getCurrentTime());
		client.dashboard.sendPacket();
		client.dashboard.showDataInTelemetry();
	}
}
