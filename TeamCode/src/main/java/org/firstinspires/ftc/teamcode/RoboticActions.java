package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@Deprecated
public class RoboticActions extends Robot{
	public RoboticActions(HardwareMap hardwareMap, RunningMode state, Client client) {
		super(hardwareMap, state, client);
	}
}
