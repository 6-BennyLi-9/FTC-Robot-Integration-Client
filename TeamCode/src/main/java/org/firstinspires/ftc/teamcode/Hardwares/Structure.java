package org.firstinspires.ftc.teamcode.Hardwares;

import static org.firstinspires.ftc.teamcode.Params.factorIntakePower;
import static org.firstinspires.ftc.teamcode.Params.factorSuspensionArmPower;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Servos;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.ClipPosition;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyTag;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.UnKnownErrorsException;

public class Structure {
	public Motors motors;
	public Servos servos;
	
	ClipPosition clipPosition;

	public Structure(Motors motors,Servos servos){
		this.motors=motors;
		this.servos=servos;
	}

	public void openFrontClip(){
		servos.FrontClipPosition=Params.ServoConfigs.frontClipOpen;
	}
	public void openRearClip(){
		servos.FrontClipPosition=Params.ServoConfigs.rearClipOpen;
	}
	public void closeFrontClip(){
		servos.FrontClipPosition=Params.ServoConfigs.frontClipClose;
	}
	public void closeRearClip(){
		servos.FrontClipPosition=Params.ServoConfigs.rearClipClose;
	}

	public void openClips(){
		openFrontClip();
		openRearClip();

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			servos.update();
		}
	}
	public void closeClips(){
		closeFrontClip();
		closeRearClip();

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			servos.update();
		}
	}
	public void clipOption(@NonNull ClipPosition clipPosition){
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
