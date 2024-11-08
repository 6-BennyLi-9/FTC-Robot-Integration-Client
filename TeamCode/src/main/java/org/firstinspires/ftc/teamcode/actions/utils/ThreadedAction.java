package org.firstinspires.ftc.teamcode.actions.utils;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 多线程的 {@code Action} 块，对 {@code tps} 要求较高
 */
public final class ThreadedAction implements Action {
	public final List<Action> actions;

	public ThreadedAction(@NonNull final List<Action> actions){
		this.actions=actions;
	}
	@UserRequirementFunctions
	public ThreadedAction(final Action... actions){
		this(Arrays.asList(actions));
	}

	@Override
	public boolean run() {
		final Set<Action> removes=new HashSet<>();
		for(final Action action:actions){
			if(!action.run()){
				removes.add(action);
			}
		}
		actions.removeAll(removes);
		return !actions.isEmpty();
	}
}
