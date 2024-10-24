package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

@Autonomous(name = "DashboardTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class DashboardTest extends TuningProgramTemplate {
	@Override
	public void whenInit() {
		client.dashboard.DrawLine(new Vector2d(1,1),new Vector2d(2,2));
		timer.restart();
	}

	@Override
	public void whileActivating() {
		client.dashboard.DrawRobot(new Position2d(0, Functions.roundClip(timer.restartAndGetDeltaTime(),64),114), DashboardClient.Blue);
	}
}
