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
	public Client client=new Client(this.telemetry);

	@Override
	public void runOpMode() throws InterruptedException {
		this.telemetryVersion();
	}

	public void telemetryVersion(){
		this.telemetry.addLine("这一行不会一直显示，如果自动程序开始，就会消失")
				.addData("一个没有用的数据","但是会在自动程序开始后消失");

		this.waitForStart();
		while (this.opModeIsActive()){
			this.telemetry.addLine("这一行会在程序中一直显示");
			this.telemetry.addData("一个没有用的数据","但是不会消失");
			this.telemetry.update();
		}
	}
	public void telemetryVersion2(){
		this.telemetry.addLine("这一行不会一直显示，如果自动程序开始，就会消失")
				.addData("一个没有用的数据","但是会在自动程序开始后消失");

		this.waitForStart();
		if (this.opModeIsActive()){
			this.telemetry.addLine("这一行会在1s后显示，当你看到时，会在1s后显示下一行");
			this.telemetry.addData("一个没有用的数据","会在1s后消失，会在1s后显示下一行");
			this.telemetry.update();

			this.sleep(1000);

			this.telemetry.addLine("从这行开始，假设你的程序正在执行，并往里面加入了关于 telemetry 的代码");
			this.telemetry.addLine("那么当你看到这一行的时候，数据已经延迟了 1s");

			this.telemetry.addData("像这样","这里是数据");
			this.sleep(1000);
		}
	}

}
