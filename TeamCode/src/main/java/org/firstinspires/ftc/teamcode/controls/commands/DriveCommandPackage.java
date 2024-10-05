package org.firstinspires.ftc.teamcode.controls.commands;

import org.firstinspires.ftc.teamcode.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.controls.definition.DriveOrderPackage;

import java.util.LinkedList;

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
