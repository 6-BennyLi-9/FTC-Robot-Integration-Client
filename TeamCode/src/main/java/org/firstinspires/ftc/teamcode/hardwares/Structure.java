package org.firstinspires.ftc.teamcode.hardwares;

import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.HighPlacement;
import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.IDLEPlacement;
import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.LowPlacement;
import static org.firstinspires.ftc.teamcode.Params.factorIntakePower;
import static org.firstinspires.ftc.teamcode.Params.factorSuspensionArmPower;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.actions.MotorControllerAction;
import org.firstinspires.ftc.teamcode.actions.StructureActions;
import org.firstinspires.ftc.teamcode.hardwares.basic.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.exceptions.UnKnownErrorsException;

/**
 * 集成化的上层控制程序
 *
 * @see Motors
 * @see Servos
 */
public class Structure {
	public Motors motors;
	public Servos servos;
	
	public ClipPosition clipPosition;
	public LiftPosition liftPosition;
	public StructureActions actions;

	public Structure(Motors motors,Servos servos){
		this.motors=motors;
		this.servos=servos;
		actions=new StructureActions(this);
	}

	/** actions.openFrontClip() */
	public void openFrontClip(){
		servos.FrontClipPosition=Params.ServoConfigs.frontClipOpen;
	}
	/** actions.openRearClip() */
	public void openRearClip(){
		servos.FrontClipPosition=Params.ServoConfigs.rearClipOpen;
	}
	/** actions.closeFrontClip() */
	public void closeFrontClip(){
		servos.FrontClipPosition=Params.ServoConfigs.frontClipClose;
	}
	/** actions.closeRearClip() */
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

	@UserRequirementFunctions
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
	@UserRequirementFunctions
	@ExtractedInterfaces
	public Action clipOptionAction(@NonNull ClipPosition clipPosition){
		if(this.clipPosition==clipPosition){
			return null;
		}else{
			switch (clipPosition){
				case Open:
					return actions.openClips();
				case Close:
					return actions.closeClips();
				default:
					throw new UnKnownErrorsException("UnKnown ClipPosition");
			}
		}
	}


	@UserRequirementFunctions
	public void armOption(@NonNull LiftPosition liftPosition){
		this.liftPosition=liftPosition;
		switch (liftPosition) {
			case IDLE:
				motors.placementArm().setTargetPosition(IDLEPlacement);
				break;
			case Low:
				motors.placementArm().setTargetPosition(LowPlacement);
				break;
			case High:
				motors.placementArm().setTargetPosition(HighPlacement);
				break;
			default:
				throw new UnKnownErrorsException("UnKnown LiftPosition");
		}
	}
	@UserRequirementFunctions
	@ExtractedInterfaces
	public Action armOptionAction(@NonNull LiftPosition liftPosition){
		if(this.liftPosition==liftPosition){
			return null;
		}else{
			switch (liftPosition){
				case IDLE:
					return new MotorControllerAction(motors.placementArm(), IDLEPlacement);
				case Low:
					return new MotorControllerAction(motors.placementArm(), LowPlacement);
				case High:
					return new MotorControllerAction(motors.placementArm(), HighPlacement);
				default:
					throw new UnKnownErrorsException("UnKnown LiftPosition");
			}
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
