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
		arc=new ArcOrganizedOdometer();
		rubbish=new SuperRubbishUselessAwfulOdometer();
		classic=new ClassicOdometer();
		integral=new IntegralOrganizedOdometer();

		arc.setColor(DashboardClient.Blue);
		integral.setColor(DashboardClient.Green);
		rubbish.setColor(DashboardClient.Red);
		classic.setColor(DashboardClient.Gray);

		robot.registerGamepad(gamepad1,gamepad2);
		robot.gamepad.keyMap =new KeyMap();
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod)
							.loadRodContent(KeyTag.ChassisRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod)
							.loadRodContent(KeyTag.ChassisTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
	}

	@Override
	public void whileActivating() {
		robot.chassis.motors.simpleMotorPowerController(
				robot.gamepad.getRodState(KeyTag.ChassisRunStrafe),
				robot.gamepad.getRodState(KeyTag.ChassisRunForward),
				robot.gamepad.getRodState(KeyTag.ChassisTurn)
		);

		robot.motors.updateDriveOptions();
		robot.sensors.updateEncoders();
		robot.motors.clearDriveOptions();

		double l=robot.sensors.getDeltaLateralInch(),a=robot.sensors.getDeltaAxialInch(),t=robot.sensors.getDeltaTurningDeg();

		arc.update(l,a,t);
		rubbish.update(l,a,t);
		classic.update(l,a,t);
		integral.update(l,a,t);

		print(arc);
		print(rubbish);
		print(integral);
		print(classic);

		arc.registerToDashBoard("arc");
		rubbish.registerToDashBoard("rubbish");
		integral.registerToDashBoard("integral");
		classic.registerToDashBoard("classic");

		client.dashboard.sendPacket();
	}

	public void print(@NonNull Odometry aim){
		Position2d pose=aim.getCurrentPose();
		client.changeData(aim.getClass().getSimpleName(),pose);
	}
}
