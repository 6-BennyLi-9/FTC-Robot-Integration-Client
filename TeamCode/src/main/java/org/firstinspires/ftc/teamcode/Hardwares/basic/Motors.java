package org.firstinspires.ftc.teamcode.Hardwares.basic;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Motors {
	public DcMotorEx LeftFront,RightFront,LeftRear,RightRear;

	public DcMotorEx PlacementArm,SuspensionArm,Intake;

	Motors(HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace = new namespace();

		LeftFront=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.LeftFront));
		LeftRear=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.LeftRear));
		RightFront=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.RightFront));
		RightRear=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.RightRear));

		PlacementArm=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.PlacementArm));
		SuspensionArm=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.SuspensionArm));
		Intake=hardwareMap.get(DcMotorEx.class, namespace.Hardware.get(hardware.Intake));

		//TODO:根据实际情况修改
		LeftFront.setDirection(DcMotorEx.Direction.FORWARD);
		LeftRear.setDirection(DcMotorEx.Direction.FORWARD);
		RightFront.setDirection(DcMotorEx.Direction.REVERSE);
		RightRear.setDirection(DcMotorEx.Direction.REVERSE);

		PlacementArm.setDirection(DcMotorEx.Direction.REVERSE);
		Intake.setDirection(DcMotorEx.Direction.REVERSE);
		SuspensionArm.setDirection(DcMotorEx.Direction.FORWARD);
	}
}
