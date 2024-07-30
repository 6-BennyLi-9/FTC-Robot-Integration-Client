package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Hardwares.hardware;

import java.util.HashMap;

public class namespace {
	public final HashMap<hardware, String > Hardware=new HashMap<>();

	public namespace(){
		//TODO:根据实际情况修改名称，这里的名称是根据我们的机器得到的
		Hardware.put(hardware.LeftFront,"leftFront");
		Hardware.put(hardware.LeftRear,"leftRear");
		Hardware.put(hardware.RightFront,"rightFront");
		Hardware.put(hardware.RightRear,"rightRear");

		Hardware.put(hardware.PlacementArm,"rightLift");
		Hardware.put(hardware.SuspensionArm,"rack");
		Hardware.put(hardware.Intake,"intake");

		Hardware.put(hardware.FrontClip,"frontCilp");
		Hardware.put(hardware.RearClip,"rearCilp");

		Hardware.put(hardware.imu,"imu");
	}
}
