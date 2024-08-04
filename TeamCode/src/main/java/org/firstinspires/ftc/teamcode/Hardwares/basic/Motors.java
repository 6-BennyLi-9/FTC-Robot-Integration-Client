package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.enums.hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Motors {
	public DcMotorEx LeftFront,RightFront,LeftRear,RightRear;
	public double LeftFrontPower,RightFrontPower,LeftRearPower,RightRearPower;

	public DcMotorEx PlacementArm,SuspensionArm,Intake;
	public double PlacementArmPower,SuspensionArmPower,IntakePower;

	public Motors(@NonNull HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace = new namespace();
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;

		PlacementArmPower=0;
		SuspensionArmPower=0;
		IntakePower=0;

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

	public void clearDriveOptions(){
		LeftFrontPower=0;
		RightFrontPower=0;
		LeftRearPower=0;
		RightRearPower=0;
	}
	public void updateDriveOptions(){
		LeftFront.setPower(LeftFrontPower);
		LeftRear.setPower(LeftRearPower);
		RightFront.setPower(RightFrontPower);
		RightRear.setPower(RightRearPower);
	}
	public void updateStructureOptions(){
		PlacementArm.setPower(PlacementArmPower);
		Intake.setPower(IntakePower);
		SuspensionArm.setPower(SuspensionArmPower);
	}
	public void update(){
		updateDriveOptions();
		updateStructureOptions();
	}
}
