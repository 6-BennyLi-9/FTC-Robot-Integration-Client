package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

@Autonomous(name = "DashboardClientTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class DashboardClientTest extends TuningProgramTemplate {
	@Override
	public void whenInit() {
		this.client.dashboard.drawLine(new Vector2d(1,1),new Vector2d(2,2));
		this.timer.restart();
	}

	@Override
	public void whileActivating() {
		this.client.dashboard.drawRobot(new Position2d(0, Mathematics.roundClip(this.timer.restartAndGetDeltaTime(), 64),114), DashboardClient.Blue,"aim");
		this.client.dashboard.sendPacket();
	}
}
