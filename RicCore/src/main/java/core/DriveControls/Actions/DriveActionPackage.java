package core.DriveControls.Actions;

import DriveControls.OrderDefinition.DriveOrder;
import DriveControls.OrderDefinition.DriveOrderPackage;

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

}
