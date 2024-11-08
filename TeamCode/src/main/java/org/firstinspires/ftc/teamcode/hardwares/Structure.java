package org.firstinspires.ftc.teamcode.hardwares;

import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.HighPlacement;
import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.IDLEPlacement;
import static org.firstinspires.ftc.teamcode.Params.PositionalMotorConfigs.LowPlacement;
import static org.firstinspires.ftc.teamcode.Params.factorIntakePower;
import static org.firstinspires.ftc.teamcode.Params.factorSuspensionArmPower;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.actions.ric.MotorControllerAction;
import org.firstinspires.ftc.teamcode.actions.ric.StructureActions;
import org.firstinspires.ftc.teamcode.hardwares.controllers.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Motors;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Servos;
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

	public Structure(final Motors motors, final Servos servos){
		this.motors=motors;
		this.servos=servos;
		this.actions =new StructureActions(this);
	}

	/** actions.openFrontClip() */
	public void openFrontClip(){
		this.servos.FrontClipPosition=Params.ServoConfigs.frontClipOpen;
	}
	/** actions.openRearClip() */
	public void openRearClip(){
		this.servos.FrontClipPosition=Params.ServoConfigs.rearClipOpen;
	}
	/** actions.closeFrontClip() */
	public void closeFrontClip(){
		this.servos.FrontClipPosition=Params.ServoConfigs.frontClipClose;
	}
	/** actions.closeRearClip() */
	public void closeRearClip(){
		this.servos.FrontClipPosition=Params.ServoConfigs.rearClipClose;
	}

	public void openClips(){
		this.openFrontClip();
		this.openRearClip();

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			this.servos.update();
		}
	}
	public void closeClips(){
		this.closeFrontClip();
		this.closeRearClip();

		if( Params.Configs.runUpdateWhenAnyNewOptionsAdded ){
			this.servos.update();
		}
	}

	@UserRequirementFunctions
	public void clipOption(@NonNull final ClipPosition clipPosition){
		this.clipPosition=clipPosition;
		switch (clipPosition){
			case Open:
				this.openClips();
				break;
			case Close:
				this.closeClips();
				break;
			default:
				throw new UnKnownErrorsException("UnKnown ClipPosition");
		}
	}
	@UserRequirementFunctions
	@ExtractedInterfaces
	public Action clipOptionAction(@NonNull final ClipPosition clipPosition){
		if(this.clipPosition==clipPosition){
			return null;
		}else{
			switch (clipPosition){
				case Open:
					return this.actions.openClips();
				case Close:
					return this.actions.closeClips();
				default:
					throw new UnKnownErrorsException("UnKnown ClipPosition");
			}
		}
	}


	@UserRequirementFunctions
	public void armOption(@NonNull final LiftPosition liftPosition){
		this.liftPosition=liftPosition;
		switch (liftPosition) {
			case IDLE:
				this.motors.placementArm().setTargetPosition(IDLEPlacement);
				break;
			case Low:
				this.motors.placementArm().setTargetPosition(LowPlacement);
				break;
			case High:
				this.motors.placementArm().setTargetPosition(HighPlacement);
				break;
			default:
				throw new UnKnownErrorsException("UnKnown LiftPosition");
		}
	}
	@UserRequirementFunctions
	@ExtractedInterfaces
	public Action armOptionAction(@NonNull final LiftPosition liftPosition){
		if(this.liftPosition==liftPosition){
			return null;
		}else{
			switch (liftPosition){
				case IDLE:
					return new MotorControllerAction(this.motors.placementArm(), IDLEPlacement);
				case Low:
					return new MotorControllerAction(this.motors.placementArm(), LowPlacement);
				case High:
					return new MotorControllerAction(this.motors.placementArm(), HighPlacement);
				default:
					throw new UnKnownErrorsException("UnKnown LiftPosition");
			}
		}
	}

	public void operateThroughGamePad(@NonNull final IntegrationGamepad gamepad){
		if(gamepad.getButtonRunAble(KeyTag.Pop)){
			this.clipPosition =ClipPosition.Open;
		}else{
			this.clipPosition =ClipPosition.Close;
		}

		this.motors.SuspensionArmPower= gamepad.getRodState(KeyTag.SuspensionArm) * factorSuspensionArmPower;
		this.motors.IntakePower= gamepad.getRodState(KeyTag.Intake) * factorIntakePower;
	}
}
