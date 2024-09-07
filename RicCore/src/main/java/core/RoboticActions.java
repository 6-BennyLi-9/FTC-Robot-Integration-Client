package core;

import com.qualcomm.robotcore.hardware.HardwareMap;

import core.Utils.Clients.Client;
import core.Utils.Enums.runningState;

public class RoboticActions extends Robot {
	public RoboticActions(HardwareMap hardwareMap, runningState state, Client client) {
		super(hardwareMap, state, client);
	}
}
