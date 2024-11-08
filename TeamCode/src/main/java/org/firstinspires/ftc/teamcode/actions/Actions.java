package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.jetbrains.annotations.Contract;

public enum Actions {
	;

	/**
	 * 标志 {@code Action} 块的最后一个节点
	 */
	public static class FinalNodeAction implements Action{
		@Override
		public boolean run() {return false;}
	}

	@UserRequirementFunctions
	public static void runAction(final Action actionBlock){
		Action recent=actionBlock;
		while(true){
			if(!recent.run()) {
				if (recent.next() instanceof FinalNodeAction) {
					break;
				}
				recent = recent.next();
			}
		}
	}

	/**
	 * 在规定时间内，如果该 {@code Action} 块仍没有结束，将会强制停止
	 */
	@UserRequirementFunctions
	public static void runTimedAllottedAction(final Action actionBlock, final long allottedMilliseconds){
		final Timer timer=new Timer();
		Action recent=actionBlock;
		timer.restart();
		while (!(timer.stopAndGetDeltaTime() >= allottedMilliseconds)) {
			if (!recent.run()) {
				if (recent.next() instanceof FinalNodeAction) {
					break;
				}
				recent = recent.next();
			}
		}
	}

	@NonNull
	@Contract("_, _ -> new")
	public static Action connectBlocking(final Action previous, final Action next){
		return new Action() {
			@Override
			public boolean run() {
				return previous.run();
			}

			@Override
			public Action next() {
				return next;
			}
		};
	}
}
