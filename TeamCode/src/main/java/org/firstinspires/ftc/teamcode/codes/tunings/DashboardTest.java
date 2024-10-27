package org.firstinspires.ftc.teamcode.codes.tunings;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Autonomous(name = "DashboardTest" , group = Params.Configs.TuningAndTuneOpModesGroup)
public class DashboardTest extends TuningProgramTemplate {
	public TelemetryPacket packet;
	public FtcDashboard dashboard;

	@Override
	public void whenInit() {
		packet=new TelemetryPacket();
		dashboard=FtcDashboard.getInstance();
	}

	@Override
	public void whileActivating() {
		packet.fieldOverlay().strokeLine(
				0,0,
				Functions.roundClip(Timer.getCurrentTime(),10),Functions.roundClip(Timer.getCurrentTime(),10)
		);
		dashboard.sendTelemetryPacket(packet);
	}
}
