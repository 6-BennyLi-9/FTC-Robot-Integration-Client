package org.firstinspires.ftc.teamcode.codes.tunings;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ClassicOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.IntegralOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.Odometry;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.SuperRubbishUselessAwfulOdometer;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;

@TeleOp(name = "MultiOdometriesTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class MultiOdometriesTest extends TuningProgramTemplate {
	public Odometry arc,rubbish,classic,integral;
	@Override
	public void whenInit() {
		this.arc =new ArcOrganizedOdometer();
		this.rubbish =new SuperRubbishUselessAwfulOdometer();
		this.classic =new ClassicOdometer();
		this.integral =new IntegralOrganizedOdometer();

		this.arc.setColor(DashboardClient.Blue);
		this.integral.setColor(DashboardClient.Green);
		this.rubbish.setColor(DashboardClient.Red);
		this.classic.setColor(DashboardClient.Gray);

		this.robot.registerGamepad(this.gamepad1, this.gamepad2);
		this.robot.gamepad.keyMap =new KeyMap();
		this.robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod)
							.loadRodContent(KeyTag.ChassisRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod)
							.loadRodContent(KeyTag.ChassisTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
	}

	@Override
	public void whileActivating() {
		this.robot.chassis.motors.simpleMotorPowerController(this.robot.gamepad.getRodState(KeyTag.ChassisRunStrafe), this.robot.gamepad.getRodState(KeyTag.ChassisRunForward), this.robot.gamepad.getRodState(KeyTag.ChassisTurn)
		);

		this.robot.motors.updateDriveOptions();
		this.robot.sensors.updateEncoders();
		this.robot.motors.clearDriveOptions();

		final double l = this.robot.sensors.getDeltaLateralInch();
		double       a =robot.sensors.getDeltaAxialInch();
		final double t = this.robot.sensors.getDeltaTurningDeg();

		this.arc.update(l,a,t);
		this.rubbish.update(l,a,t);
		this.classic.update(l,a,t);
		this.integral.update(l,a,t);

		this.print(this.arc);
		this.print(this.rubbish);
		this.print(this.integral);
		this.print(this.classic);

		this.arc.registerToDashBoard("arc");
		this.rubbish.registerToDashBoard("rubbish");
		this.integral.registerToDashBoard("integral");
		this.classic.registerToDashBoard("classic");

		this.client.dashboard.sendPacket();
	}

	public void print(@NonNull final Odometry aim){
		final Position2d pose =aim.getCurrentPose();
		this.client.changeData(aim.getClass().getSimpleName(),pose);
	}
}
