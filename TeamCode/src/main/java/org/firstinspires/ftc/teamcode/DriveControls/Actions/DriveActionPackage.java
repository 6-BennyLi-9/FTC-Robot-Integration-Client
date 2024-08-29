package org.firstinspires.ftc.teamcode.DriveControls.Actions;

import org.firstinspires.ftc.teamcode.DriveControls.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.DriveControls.DriveOrder;
import org.firstinspires.ftc.teamcode.DriveControls.DriveOrderPackage;

import java.util.LinkedList;

public class DriveActionPackage implements DriveOrderPackage {
	public LinkedList<DriveAction> actions;

	DriveActionPackage(){
		actions=new LinkedList<>();
	}

	@Override
	public LinkedList<DriveOrder> getOrder() {
		LinkedList<DriveOrder> res=new LinkedList<>();
		for (DriveOrder order : actions) {
			res.push(order);
		}
		return res;
	}

	@Override
	public void setOrder(LinkedList<DriveOrder> val) {
		if(val != null) {
			actions=new LinkedList<>();
			for (DriveOrder order : val) {
				actions.push((DriveAction) order);
			}
		}else{
			throw new NullPointerException();
		}
	}
}
