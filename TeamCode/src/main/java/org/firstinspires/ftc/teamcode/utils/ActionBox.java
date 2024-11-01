package org.firstinspires.ftc.teamcode.utils;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.utils.annotations.Beta;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.ArrayList;
import java.util.List;

public class ActionBox {
	protected List<Action> actions;

	public ActionBox(){
		this.actions =new ArrayList<>();
	}

	@UserRequirementFunctions
	public void pushAction(final Action action){
		this.actions.add(action);
	}

	@ExtractedInterfaces
	@UserRequirementFunctions
	public ParallelAction output(){
		final ParallelAction res =new ParallelAction(this.actions);
		this.actions.clear();
		return res;
	}

	@ExtractedInterfaces
	@Beta
	public ParallelAction step(){
		final List<Action> output =new ArrayList<>();
		for(final Action action: this.actions){
			output.add(new InstantAction(()->action.run(new TelemetryPacket())));
		}
		return new ParallelAction(output);
	}
}
