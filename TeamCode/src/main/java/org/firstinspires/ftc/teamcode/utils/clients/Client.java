package org.firstinspires.ftc.teamcode.utils.clients;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.samples.ClientUsage;

/**
 * @see ClientUsage
 */
public class Client extends TelemetryClient{
	public DashboardClient dashboard = new DashboardClient();
	public Client(Telemetry telemetry) {
		super(Params.Configs.clientAutoRegisteredFtcDashboardTelemetry ?
		        new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry())
		      : telemetry
		);
	}
}
