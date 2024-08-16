package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.enums.Hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Servos {
	public HardwareSet hardware;
	public double FrontClipPosition,RearClipPosition;

	private final static double AllowErrorPosition=0.1;
	private boolean PositionInPlace;

	public Servos(@NonNull HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace=new namespace();

		hardware.addDevice(hardwareMap.get(Servo.class, namespace.Hardware.get(Hardware.FrontClip)),Hardware.FrontClip);
		hardware.addDevice(hardwareMap.get(Servo.class, namespace.Hardware.get(Hardware.RearClip)),Hardware.RearClip);

		PositionInPlace=false;
	}

	public void update(){
		hardware.setPosition(Hardware.FrontClip,FrontClipPosition);
		hardware.setPosition(Hardware.RearClip,RearClipPosition);

		PositionInPlace=(Math.abs(hardware.getPosition(Hardware.RearClip) - RearClipPosition)  < AllowErrorPosition) &&
						(Math.abs(hardware.getPosition(Hardware.FrontClip)- FrontClipPosition) < AllowErrorPosition);
	}

	public boolean InPlace(){
		return PositionInPlace;
	}
}
