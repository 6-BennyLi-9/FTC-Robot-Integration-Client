package org.firstinspires.ftc.teamcode.codes.tunings;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Robot;
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
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@TeleOp(name = "MultiOdometriesTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class MultiOdometriesTest extends OpMode {
	public Client client;
	public Odometry arc,rubbish,classic,integral;
	public Robot robot;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		client=robot.client;

		arc=new ArcOrganizedOdometer();
		rubbish=new SuperRubbishUselessAwfulOdometer();
		classic=new ClassicOdometer();
		integral=new IntegralOrganizedOdometer(client);

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
	public void loop() {
		robot.chassis.motors.simpleMotorPowerController(
				robot.gamepad.getRodState(KeyTag.ChassisRunStrafe),
				robot.gamepad.getRodState(KeyTag.ChassisRunForward),
				robot.gamepad.getRodState(KeyTag.ChassisTurn)
		);

		robot.motors.updateDriveOptions();
		robot.sensors.updateEncoders();
		robot.motors.clearDriveOptions();

		arc.update(robot.sensors.getDeltaL(),robot.sensors.getDeltaA(),robot.sensors.getDeltaT());
		rubbish.update(robot.sensors.getDeltaL(),robot.sensors.getDeltaA(),robot.sensors.getDeltaT());
		classic.update(robot.sensors.getDeltaL(),robot.sensors.getDeltaA(),robot.sensors.getDeltaT());
		integral.update(robot.sensors.getDeltaL(),robot.sensors.getDeltaA(),robot.sensors.getDeltaT());

		print(arc);
		print(rubbish);
		print(integral);
		print(classic);

		arc.registerToDashBoard("arc");
		rubbish.registerToDashBoard("rubbish");
		integral.registerToDashBoard("integral");
		classic.registerToDashBoard("classic");
	}

	public void print(@NonNull Odometry aim){
		Position2d pose=aim.getCurrentPose();
		client.changeData(aim.getClass().getSimpleName(),pose);
	}
}
