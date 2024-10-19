package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.Beta;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

/**
 * 用于演示 Telemetry 和 Client 的区别
 * @see Telemetry
 * @see Client
 */
@Beta
@Autonomous(name = "CompareTelemetryAndClient",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class CompareTelemetryAndClient extends LinearOpMode {
	public Client client=new Client(telemetry);

	@Override
	public void runOpMode() throws InterruptedException {
		telemetryVersion();
	}

	public void telemetryVersion(){
		telemetry.addLine("这一行不会一直显示，如果自动程序开始，就会消失")
				.addData("一个没有用的数据","但是会在自动程序开始后消失");

		waitForStart();
		while (opModeIsActive()){
			telemetry.addLine("这一行会在程序中一直显示");
			telemetry.addData("一个没有用的数据","但是不会消失");
			telemetry.update();
		}
	}
	public void telemetryVersion2(){
		telemetry.addLine("这一行不会一直显示，如果自动程序开始，就会消失")
				.addData("一个没有用的数据","但是会在自动程序开始后消失");

		waitForStart();
		if (opModeIsActive()){
			telemetry.addLine("这一行会在1s后显示，当你看到时，会在1s后显示下一行");
			telemetry.addData("一个没有用的数据","会在1s后消失，会在1s后显示下一行");
			telemetry.update();

			sleep(1000);

			telemetry.addLine("从这行开始，假设你的程序正在执行，并往里面加入了关于 telemetry 的代码");
			telemetry.addLine("那么当你看到这一行的时候，数据已经延迟了 1s");

			telemetry.addData("像这样","这里是数据");
			sleep(1000);
		}
	}

}
