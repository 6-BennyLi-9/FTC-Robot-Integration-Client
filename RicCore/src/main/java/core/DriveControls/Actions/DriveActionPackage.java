package core.DriveControls.Actions;

import java.util.LinkedList;

import core.DriveControls.OrderDefinition.DriveOrder;
import core.DriveControls.OrderDefinition.DriveOrderPackage;

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

}
