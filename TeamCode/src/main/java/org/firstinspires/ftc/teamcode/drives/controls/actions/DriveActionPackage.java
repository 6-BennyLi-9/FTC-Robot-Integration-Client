package org.firstinspires.ftc.teamcode.drives.controls.actions;

import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;

import java.util.LinkedList;

public class DriveActionPackage implements DriveOrderPackage {
	public LinkedList<DriveAction> actions;

	DriveActionPackage(){
		this.actions =new LinkedList<>();
	}

	@Override
	public LinkedList<DriveOrder> getOrder() {
		final LinkedList<DriveOrder> res =new LinkedList<>();
		for (final DriveOrder order : this.actions) {
			res.push(order);
		}
		return res;
	}

}
