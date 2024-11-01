package org.firstinspires.ftc.teamcode.drives.controls.commands;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;

import java.util.LinkedList;

/**
 * 为了方便存储，查询
 */
public class DriveCommandPackage implements DriveOrderPackage {
	public LinkedList<DriveCommand> commands;

	public DriveCommandPackage() {
		this.commands = new LinkedList<>();
	}

	@Override
	public LinkedList<DriveOrder> getOrder() {
		final LinkedList<DriveOrder> res =new LinkedList<>();
		for (final DriveOrder order : this.commands) {
			res.push(order);
		}
		return res;
	}

}
