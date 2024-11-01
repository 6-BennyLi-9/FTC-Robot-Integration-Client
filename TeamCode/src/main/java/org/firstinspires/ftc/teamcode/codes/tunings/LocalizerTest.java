package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.drives.localizers.plugins.DeadWheelLocalizer;

@TeleOp(name = "LocalizerTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class LocalizerTest extends TuningProgramTemplate {
	public PositionLocalizerPlugin localizer;

	@Override
	public void whenInit() {
//		localizer= PluginMerger.mergePlugins(new DeadWheelLocalizer(robot.sensors),new BNOHeadingLocalizer(robot.chassis), DashboardClient.Blue,"114");
		this.localizer =new DeadWheelLocalizer(this.robot.sensors);
	}

	@Override
	public void whileActivating() {
		this.localizer.update();
		this.client.changeData("current pose", this.localizer.getCurrentPose());
	}
}
