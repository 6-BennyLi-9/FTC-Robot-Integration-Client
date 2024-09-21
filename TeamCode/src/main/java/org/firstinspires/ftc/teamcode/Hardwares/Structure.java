package org.firstinspires.ftc.teamcode.Hardwares;

import static org.firstinspires.ftc.teamcode.Params.factorIntakePower;
import static org.firstinspires.ftc.teamcode.Params.factorSuspensionArmPower;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Servos;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Enums.ClipPosition;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyTag;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.UnKnownErrorsException;

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

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			servos.update();
		}
	}
	private void closeClips(){
		CloseFrontClip();
		CloseRearClip();

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
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

	public void operateThroughGamePad(@NonNull IntegrationGamepad gamepad){
		if(gamepad.getButtonRunAble(KeyTag.Pop)){
			clipPosition=ClipPosition.Open;
		}else{
			clipPosition=ClipPosition.Close;
		}

		motors.SuspensionArmPower=gamepad.getRodState(KeyTag.SuspensionArm)* factorSuspensionArmPower;
		motors.IntakePower=gamepad.getRodState(KeyTag.Intake)* factorIntakePower;
	}
}
