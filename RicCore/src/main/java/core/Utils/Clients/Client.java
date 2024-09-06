package core.Utils.Clients;

import core.robotcore.external.Telemetry;

/**
 * @see core.teamcode.RIC_samples.ClientUsage
 */
public class Client extends TelemetryClient{
	public DashboardClient dashboard;
	public Client(Telemetry telemetry) {
		super(telemetry);
		dashboard =new DashboardClient();
	}
}
