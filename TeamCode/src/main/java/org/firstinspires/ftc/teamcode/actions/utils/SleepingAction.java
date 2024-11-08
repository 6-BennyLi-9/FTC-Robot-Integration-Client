package org.firstinspires.ftc.teamcode.actions.utils;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

public final class SleepingAction implements Action {
	private final Timer timer=new Timer();
	private final long sleepMilliseconds;

	@UserRequirementFunctions
	public SleepingAction(final long sleepMilliseconds){
		this.sleepMilliseconds = sleepMilliseconds;
		timer.restart();
	}

	@Override
	public boolean run() {
		return timer.stopAndGetDeltaTime()< sleepMilliseconds;
	}
}
