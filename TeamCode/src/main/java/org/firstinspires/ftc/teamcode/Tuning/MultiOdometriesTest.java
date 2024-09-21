package org.firstinspires.ftc.teamcode.Tuning;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Localizers.Odometry.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.ClassicOdometer;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.IntegralOrganizedOdometer;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.Odometry;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.SuperRubbishUselessAwfulOdometer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Clients.DashboardClient;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;

@TeleOp(name = "MultiOdometriesTest",group = "tune")
public class MultiOdometriesTest extends OpMode {
	public Client client;
	public Odometry arc,rubbish,classic,integral;
	public Robot robot;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.ManualDrive,telemetry);
		client=robot.client;

		arc=new ArcOrganizedOdometer(client);
		rubbish=new SuperRubbishUselessAwfulOdometer(client);
		classic=new ClassicOdometer(client);
		integral=new IntegralOrganizedOdometer(client);

		arc.setColor(DashboardClient.Blue);
		integral.setColor(DashboardClient.Green);
		rubbish.setColor(DashboardClient.Red);
		classic.setColor(DashboardClient.Gray);
	}

	@Override
	public void loop() {
		robot.sensors.updateEncoders();
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
