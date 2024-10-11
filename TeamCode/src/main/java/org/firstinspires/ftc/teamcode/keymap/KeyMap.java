package org.firstinspires.ftc.teamcode.keymap;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.BasicIntegrationGamepad;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class KeyMap {
	public final Map<KeyTag, KeyMapContent> contents;
	public KeyMap(){
		contents=new HashMap<>();
	}

	public void initKeys(){
		contents.clear();

		//TODO: 填入需求键位
		loadRodContent(ClassicRunForward,       LeftStickY,     PullRod);
		loadRodContent(ClassicRunStrafe,        LeftStickY,     PullRod);
		loadRodContent(ClassicTurn,             RightStickY,    PullRod);

		loadButtonContent(Intake,               A,          RunWhenButtonHold,  false);
		loadButtonContent(Pop,                  B,          RunWhenButtonHold,  false);

		loadButtonContent(ArmIDLE,              X,          RunWhenButtonPressed,   false);
		loadButtonContent(ArmInIntake,          Y,          RunWhenButtonPressed,   false);
		loadButtonContent(ArmLowerPlacement,    DpadDown,   RunWhenButtonPressed,   false);
		loadButtonContent(ArmHigherPlacement,   DpadUp,     RunWhenButtonPressed,   false);
	}

	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	public void loadButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting){
		if(contents.containsKey(tag)){
			contents.replace(tag,new KeyMapButtonContent(tag,type,setting));
		}else{
			contents.put(tag,new KeyMapButtonContent(tag,type,setting));
		}
	}
	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	public void loadRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting){
		if(contents.containsKey(tag)){
			contents.replace(tag,new KeyMapRodContent(tag,type,setting));
		}else{
			contents.put(tag,new KeyMapRodContent(tag,type,setting));
		}
	}

	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void loadButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting,boolean IsGamePad1){
		if(contents.containsKey(tag)){
			contents.replace(tag,new KeyMapButtonContent(tag,type,setting,IsGamePad1));
		}else{
			contents.put(tag,new KeyMapButtonContent(tag,type,setting,IsGamePad1));
		}
	}
	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void loadRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting,boolean IsGamePad1){
		if(contents.containsKey(tag)){
			contents.replace(tag,new KeyMapRodContent(tag,type,setting,IsGamePad1));
		}else{
			contents.put(tag,new KeyMapRodContent(tag,type,setting,IsGamePad1));
		}
	}

	@ExtractedInterfaces
	public boolean getButtonStateFromTagAndGamePad(KeyTag tag, @NonNull BasicIntegrationGamepad gamepad){
		return gamepad.getButtonState(Objects.requireNonNull((KeyMapButtonContent)contents.get(tag)).type, Objects.requireNonNull(contents.get(tag)).setting);
	}
	@ExtractedInterfaces
	public double getRodStateFromTagAndGamePad(KeyTag tag, @NonNull BasicIntegrationGamepad gamepad){
		return gamepad.getRodState(Objects.requireNonNull((KeyMapRodContent)contents.get(tag)).type);
	}

	@ExtractedInterfaces
	public boolean IsControlledByGamepad1(KeyTag tag){
		return Objects.requireNonNull(contents.get(tag)).IsControlledByGamePad1;
	}

	@ExtractedInterfaces
	public boolean containsKeySetting(KeyTag tag){
		return contents.containsKey(tag);
	}
}
