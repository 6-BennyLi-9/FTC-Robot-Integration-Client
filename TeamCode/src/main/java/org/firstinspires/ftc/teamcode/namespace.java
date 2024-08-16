package org.firstinspires.ftc.teamcode;

import java.util.HashMap;

public class namespace {
	public final HashMap<org.firstinspires.ftc.teamcode.utils.enums.Hardware, String > Hardware=new HashMap<>();

	public namespace(){
		//TODO:根据实际情况修改名称，这里的名称是根据我们的机器得到的
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.LeftFront,"leftFront");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.LeftRear,"leftRear");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.RightFront,"rightFront");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.RightRear,"rightRear");

		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.PlacementArm,"rightLift");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.SuspensionArm,"rack");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.Intake,"intake");

		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.FrontClip,"frontCilp");
		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.RearClip,"rearCilp");

		Hardware.put(org.firstinspires.ftc.teamcode.utils.enums.Hardware.imu,"imu");
	}
}
