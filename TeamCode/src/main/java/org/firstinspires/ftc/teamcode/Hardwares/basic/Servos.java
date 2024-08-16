package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.enums.Hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Servos {
	public Servo FrontClip,RearClip;
	public double FrontClipPosition,RearClipPosition;

	private final static double AllowErrorPosition=0.1;
	private boolean PositionInPlace;

	public Servos(@NonNull HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace=new namespace();

		FrontClip=hardwareMap.get(Servo.class, namespace.Hardware.get(Hardware.FrontClip));
		RearClip=hardwareMap.get(Servo.class, namespace.Hardware.get(Hardware.RearClip));

		PositionInPlace=false;
	}

	public void update(){
		FrontClip.setPosition(FrontClipPosition);
		RearClip.setPosition(RearClipPosition);

		PositionInPlace=(Math.abs(RearClip.getPosition() - RearClipPosition)  < AllowErrorPosition) &&
						(Math.abs(FrontClip.getPosition()- FrontClipPosition) < AllowErrorPosition);
	}

	public boolean InPlace(){
		return PositionInPlace;
	}
}
