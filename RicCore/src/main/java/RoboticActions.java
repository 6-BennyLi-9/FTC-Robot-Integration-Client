import com.qualcomm.robotcore.hardware.HardwareMap;

import Utils.Clients.Client;
import Utils.Enums.runningState;

public class RoboticActions extends Robot {
	public RoboticActions(HardwareMap hardwareMap, runningState state, Client client) {
		super(hardwareMap, state, client);
	}
}
