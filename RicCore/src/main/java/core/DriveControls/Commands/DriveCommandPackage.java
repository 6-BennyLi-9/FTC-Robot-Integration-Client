package core.DriveControls.Commands;

import java.util.LinkedList;

import core.DriveControls.OrderDefinition.DriveOrder;
import core.DriveControls.OrderDefinition.DriveOrderPackage;

/**
 * 为了方便存储，查询
 */
public class DriveCommandPackage implements DriveOrderPackage {
	public LinkedList<DriveCommand> commands;

	public DriveCommandPackage() {
		commands = new LinkedList<>();
	}

	@Override
	public LinkedList<DriveOrder> getOrder() {
		LinkedList<DriveOrder> res=new LinkedList<>();
		for (DriveOrder order : commands) {
			res.push(order);
		}
		return res;
	}

}
