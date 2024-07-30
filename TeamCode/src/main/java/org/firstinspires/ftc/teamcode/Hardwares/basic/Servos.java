package org.firstinspires.ftc.teamcode.Hardwares.basic;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.hardware;
import org.firstinspires.ftc.teamcode.namespace;

public class Servos {
	public Servo FrontClip,RearClip;
	public double FrontClipPosition,RearClipPosition;

	public Servos(HardwareMap hardwareMap){
		org.firstinspires.ftc.teamcode.namespace namespace=new namespace();

		FrontClip=hardwareMap.get(Servo.class, namespace.Hardware.get(hardware.FrontClip));
		RearClip=hardwareMap.get(Servo.class, namespace.Hardware.get(hardware.RearClip));
	}

	public void update(){
		FrontClip.setPosition(FrontClipPosition);
		RearClip.setPosition(RearClipPosition);
	}
}
