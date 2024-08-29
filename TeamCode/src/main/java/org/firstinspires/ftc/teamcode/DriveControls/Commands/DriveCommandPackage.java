package org.firstinspires.ftc.teamcode.DriveControls.Commands;

import org.firstinspires.ftc.teamcode.DriveControls.DriveOrder;
import org.firstinspires.ftc.teamcode.DriveControls.DriveOrderPackage;

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

	@Override
	public void setOrder(LinkedList<DriveOrder> val) {
		if(val != null) {
			commands=new LinkedList<>();
			for (DriveOrder order : val) {
				commands.push((DriveCommand) order);
			}
		}else{
			throw new NullPointerException();
		}
	}
}
