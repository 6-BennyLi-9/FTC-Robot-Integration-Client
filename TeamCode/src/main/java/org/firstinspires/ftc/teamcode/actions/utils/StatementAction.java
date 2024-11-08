package org.firstinspires.ftc.teamcode.actions.utils;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

public class StatementAction implements Action {
	public interface StatementNode{
		void run();
	}
	private final StatementNode node;

	/**
	 * 语句式 {@code Action} 块
	 * <p>
	 * 示例： {@code ... new StatementAction( () -> yourMethodHere() ); ...}
	 */
	@UserRequirementFunctions
	public StatementAction(final StatementNode meaning){
		node=meaning;
	}

	@Override
	public boolean run() {
		node.run();
		return false;
	}
}
