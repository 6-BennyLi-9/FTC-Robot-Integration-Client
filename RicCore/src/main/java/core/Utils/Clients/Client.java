package core.Utils.Clients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Client extends TelemetryClient{
	public DashboardClient dashboard;
	public Client(Telemetry telemetry) {
		super(telemetry);
		dashboard =new DashboardClient();
	}
}
