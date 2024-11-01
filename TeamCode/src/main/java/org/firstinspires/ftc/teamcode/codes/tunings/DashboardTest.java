package org.firstinspires.ftc.teamcode.codes.tunings;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Timer;

@Autonomous(name = "DashboardTest" , group = Params.Configs.TuningAndTuneOpModesGroup)
public class DashboardTest extends TuningProgramTemplate {
	public TelemetryPacket packet;

	@Override
	public void whenInit() {
		this.packet =new TelemetryPacket(true);
		FtcDashboard.getInstance().sendTelemetryPacket(this.packet);
	}

	@Override
	public void whileActivating() {
		this.packet =new TelemetryPacket(true);
		this.packet.fieldOverlay().strokeLine(
				0,0, Mathematics.roundClip(Timer.getCurrentTime(), 10), Mathematics.roundClip(Timer.getCurrentTime(), 10)
		);
		FtcDashboard.getInstance().sendTelemetryPacket(this.packet);
	}
}
