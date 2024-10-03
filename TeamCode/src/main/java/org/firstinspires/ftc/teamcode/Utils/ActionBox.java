package org.firstinspires.ftc.teamcode.Utils;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

import java.util.ArrayList;
import java.util.List;

public class ActionBox {
	protected List<Action> actions;

	public ActionBox(){
		actions=new ArrayList<>();
	}

	@UserRequirementFunctions
	public void pushAction(Action action){
		actions.add(action);
	}

	@ExtractedInterfaces
	@UserRequirementFunctions
	public ParallelAction output(){
		ParallelAction res=new ParallelAction(actions);
		actions.clear();
		return res;
	}
}