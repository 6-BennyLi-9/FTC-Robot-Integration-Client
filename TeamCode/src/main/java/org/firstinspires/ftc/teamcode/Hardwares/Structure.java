package org.firstinspires.ftc.teamcode.Hardwares;

import static org.firstinspires.ftc.teamcode.Params.factorIntakePower;
import static org.firstinspires.ftc.teamcode.Params.factorSuspensionArmPower;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.RuntimeOption;
import org.firstinspires.ftc.teamcode.utils.Enums.ClipPosition;
import org.firstinspires.ftc.teamcode.utils.Exceptions.UnKnownErrorsException;

public class Structure {
	Motors motors;
	Servos servos;
	
	ClipPosition clipPosition;

	public Structure(Motors motors,Servos servos){
		this.motors=motors;
		this.servos=servos;
	}
//  TODO:测量这些值
	public void OpenFrontClip(){
		servos.FrontClipPosition=0;
	}
	public void OpenRearClip(){
		servos.FrontClipPosition=0;
	}
	public void CloseFrontClip(){
		servos.FrontClipPosition=0;
	}
	public void CloseRearClip(){
		servos.FrontClipPosition=0;
	}

	private void openClips(){
		OpenFrontClip();
		OpenRearClip();

		if( RuntimeOption.runUpdateWhenAnyNewOptionsAdded ){
			servos.update();
		}
	}
	private void closeClips(){
		CloseFrontClip();
		CloseRearClip();

		if( RuntimeOption.runUpdateWhenAnyNewOptionsAdded ){
			servos.update();
		}
	}
	public void ClipOption(@NonNull ClipPosition clipPosition){
		this.clipPosition=clipPosition;
		switch (clipPosition){
			case Open:
				openClips();
				break;
			case Close:
				closeClips();
				break;
			default:
				throw new UnKnownErrorsException("UnKnown ClipPosition");
		}
	}
	
	private boolean gamePadButtonBHolding=false;
	public void operateThroughGamePad(@NonNull Gamepad gamepad){
		if(gamepad.b){
			if(!gamePadButtonBHolding) {
				gamePadButtonBHolding = true;
				switch (clipPosition) {
					case Open:
						clipPosition=ClipPosition.Close;
						break;
					case Close:
						clipPosition=ClipPosition.Open;
						break;
					default:
						throw new UnKnownErrorsException("UnKnown ClipPosition");
				}
			}
		}else gamePadButtonBHolding=false;
		
		motors.SuspensionArmPower=gamepad.right_stick_y* factorSuspensionArmPower;
		motors.IntakePower=gamepad.left_stick_y* factorIntakePower;
	}
}
