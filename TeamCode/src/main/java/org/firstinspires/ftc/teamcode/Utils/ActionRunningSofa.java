package org.firstinspires.ftc.teamcode.Utils;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

import java.util.ArrayList;
import java.util.List;

public class ActionRunningSofa {
	protected List<Action> actions;

	public ActionRunningSofa(){
		actions=new ArrayList<>();
	}

	@UserRequirementFunctions
	public void pushAction(Action action){
		actions.add(action);
	}

	@ExtractedInterfaces
	@UserRequirementFunctions
	public ParallelAction output(){
		return new ParallelAction(actions);
	}
}
