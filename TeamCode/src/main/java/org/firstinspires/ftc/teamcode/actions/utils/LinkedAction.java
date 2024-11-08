package org.firstinspires.ftc.teamcode.actions.utils;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.Actions;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.Arrays;
import java.util.List;

public final class LinkedAction implements Action{
	private final List<Action> actions;

	public LinkedAction(@NonNull final List<Action> actions){
		this.actions=actions;
	}
	@UserRequirementFunctions
	public LinkedAction(final Action... actions){
		this(Arrays.asList(actions));
	}

	@Override
	public boolean run() {
		Actions.runAction(actions.get(0));
		actions.remove(0);
		return !actions.isEmpty();
	}
}
