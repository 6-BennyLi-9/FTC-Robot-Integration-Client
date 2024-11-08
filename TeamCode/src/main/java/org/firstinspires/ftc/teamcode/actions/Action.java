package org.firstinspires.ftc.teamcode.actions;

public interface Action {
	/**
	 * @return 该 {@code Action} 块是否结束
	 */
	boolean run();
	default Action next(){return new Actions.FinalNodeAction();}
}
