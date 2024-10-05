package org.firstinspires.ftc.teamcode.utils.clients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * @see org.firstinspires.ftc.teamcode.samples.ClientUsage
 */
//TODO 测试 NoSortTelemetryClient
public class Client extends TelemetryClient{
	public DashboardClient dashboard;
	public Client(Telemetry telemetry) {
		super(telemetry);
		dashboard =new DashboardClient();
	}
}
