package org.firstinspires.ftc.teamcode.Hardwares.basic;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.enums.HardwareType;
import org.firstinspires.ftc.teamcode.namespace;

public class Servos {
	public HardwareSet hardware;
	public double FrontClipPosition,RearClipPosition;

	private final static double AllowErrorPosition=0.1;
	private boolean PositionInPlace;

	public Servos(@NonNull HardwareMap hardwareMap){
		this(hardwareMap,new HardwareSet());
	}
	public Servos(@NonNull HardwareMap hardwareMap,HardwareSet hardware){
		namespace namespace=new namespace();
		this.hardware=hardware;

		this.hardware.addDevice(hardwareMap.get(Servo.class, namespace.Hardware.get(HardwareType.FrontClip)), HardwareType.FrontClip);
		this.hardware.addDevice(hardwareMap.get(Servo.class, namespace.Hardware.get(HardwareType.RearClip)), HardwareType.RearClip);

		PositionInPlace=false;
	}

	public void update(){
		hardware.setPosition(HardwareType.FrontClip,FrontClipPosition);
		hardware.setPosition(HardwareType.RearClip,RearClipPosition);

		PositionInPlace=(Math.abs(hardware.getPosition(HardwareType.RearClip) - RearClipPosition)  < AllowErrorPosition) &&
						(Math.abs(hardware.getPosition(HardwareType.FrontClip)- FrontClipPosition) < AllowErrorPosition);
	}

	/**
	 * @return 是否所有舵机都到位了
	 */
	public boolean InPlace(){
		return PositionInPlace;
	}
}
