package org.firstinspires.ftc.teamcode.codes.tunings;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.KeyMap;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.ClassicOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.IntegralOrganizedOdometer;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.Odometry;
import org.firstinspires.ftc.teamcode.drives.localizers.odometries.SuperRubbishUselessAwfulOdometer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@TeleOp(name = "MultiOdometriesTest",group = "tune")
public class MultiOdometriesTest extends OpMode {
	public Client client;
	public Odometry arc,rubbish,classic,integral;
	public Robot robot;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		client=robot.client;

		client.addLine("按[A]进入直线模式，该模式下机器将无法全方向平移");

		arc=new ArcOrganizedOdometer(client);
		rubbish=new SuperRubbishUselessAwfulOdometer(client);
		classic=new ClassicOdometer(client);
		integral=new IntegralOrganizedOdometer(client);

		arc.setColor(DashboardClient.Blue);
		integral.setColor(DashboardClient.Green);
		rubbish.setColor(DashboardClient.Red);
		classic.setColor(DashboardClient.Gray);

		robot.registerGamepad(gamepad1,gamepad2);
		robot.gamepad.keyMap =new KeyMap();
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.SinglePressToChangeRunAble);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.ClassicSpeedConfig, KeyButtonType.X, KeyMapSettingType.SinglePressToChangeRunAble);
	}

	boolean LinerMode=false;

	@Override
	public void loop() {
		LinerMode=robot.gamepad.getButtonRunAble(KeyTag.TuningButton1);
		client.changeData("直线模式",LinerMode);
		if(robot.gamepad.getButtonRunAble(KeyTag.ClassicSpeedConfig)){
			robot.motors.setBufPower(0.9);
		}else{
			robot.motors.setBufPower(0.3);
		}
		if(LinerMode){
			double x=robot.gamepad.getRodState(KeyTag.ClassicRunForward);
			double y=robot.gamepad.getRodState(KeyTag.ClassicRunStrafe);
			double t=robot.gamepad.getRodState(KeyTag.ClassicTurn);
			if(Math.abs(x)> Math.abs(y)){
				robot.motors.simpleMotorPowerController(x,0,t);
			}else{
				robot.motors.simpleMotorPowerController(0,y,t);
			}
		}
		robot.update();

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
		Pose2d pose=aim.getCurrentPose();
		client.changeData(aim.getClass().getName(),pose.position.x+","+pose.position.y+"|"+pose.heading.toDouble());
	}
}
