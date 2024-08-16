package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.utils.enums.HardwareType;

import java.util.HashMap;

public class namespace {
	public final HashMap<HardwareType, String > Hardware=new HashMap<>();

	public namespace(){
		//TODO:根据实际情况修改名称，这里的名称是根据我们的机器得到的
		Hardware.put(HardwareType.LeftFront,"leftFront");
		Hardware.put(HardwareType.LeftRear,"leftRear");
		Hardware.put(HardwareType.RightFront,"rightFront");
		Hardware.put(HardwareType.RightRear,"rightRear");

		Hardware.put(HardwareType.PlacementArm,"rightLift");
		Hardware.put(HardwareType.SuspensionArm,"rack");
		Hardware.put(HardwareType.Intake,"intake");

		Hardware.put(HardwareType.FrontClip,"frontCilp");
		Hardware.put(HardwareType.RearClip,"rearCilp");

		Hardware.put(HardwareType.imu,"imu");
	}
}
