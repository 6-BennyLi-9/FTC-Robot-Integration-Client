package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.utils.ThreadedAction;
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
	public ThreadedAction output(){
		final ThreadedAction res =new ThreadedAction(this.actions);
		this.actions.clear();
		return res;
	}

	@ExtractedInterfaces
	@Beta
	public ThreadedAction step(){
		final List<Action> output =new ArrayList<>();
		for(final Action action: this.actions){
			output.add(new ThreadedAction(action));
		}
		return new ThreadedAction(output);
	}
}
