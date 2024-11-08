package org.firstinspires.ftc.teamcode.actions.utils;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.PriorityAction;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据 {@code PriorityAction} 的优先级排序后进行执行操作
 */
public class PriorityThreadedAction implements Action {
	public final List<PriorityAction> actions;

	public PriorityThreadedAction(@NonNull final List<PriorityAction> actions){
		this.actions=actions;
		actions.sort(Comparator.comparingLong(x -> -x.getPriorityCode()));
	}
	@UserRequirementFunctions
	public PriorityThreadedAction(final PriorityAction... actions){
		this(Arrays.asList(actions));
	}

	@Override
	public boolean run() {
		final Set<PriorityAction> removes=new HashSet<>();
		for(final PriorityAction action:actions){
			if(!action.run()){
				removes.add(action);
			}
		}
		actions.removeAll(removes);
		return !actions.isEmpty();
	}
}
