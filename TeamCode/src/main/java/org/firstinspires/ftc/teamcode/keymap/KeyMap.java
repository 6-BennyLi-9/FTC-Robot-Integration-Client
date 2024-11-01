package org.firstinspires.ftc.teamcode.keymap;

import static org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag.*;
import static org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType.*;
import static org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType.*;
import static org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType.*;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Global;
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

/**
 * 基于手柄的键位类
 */
public final class KeyMap {
	public final Map<KeyTag, KeyMapContent> contents;
	public KeyMap(){
		this.contents =new HashMap<>();
	}

	public void initKeys(){
		this.contents.clear();

		//TODO: 填入需求键位
		this.loadRodContent(ChassisRunForward,       LeftStickY,     PullRod)
		.loadRodContent(ChassisRunStrafe,        LeftStickY,     PullRod)
		.loadRodContent(ChassisTurn,             RightStickY,    PullRod);

		this.loadButtonContent(Intake,               A,          RunWhenButtonHold,  false)
		.loadButtonContent(Pop,                  B,          RunWhenButtonHold,  false);

		this.loadButtonContent(ArmIDLE,              X,          RunWhenButtonPressed,   false)
		.loadButtonContent(ArmInIntake,          Y,          RunWhenButtonPressed,   false)
		.loadButtonContent(ArmLowerPlacement,    DpadDown,   RunWhenButtonPressed,   false)
		.loadButtonContent(ArmHigherPlacement,   DpadUp,     RunWhenButtonPressed,   false);
	}

	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	public KeyMap loadButtonContent(final KeyTag tag, final KeyButtonType type, final KeyMapSettingType setting){
		if(this.contents.containsKey(tag)){
			this.contents.replace(tag,new KeyMapButtonContent(tag,type,setting));
		}else{
			this.contents.put(tag,new KeyMapButtonContent(tag,type,setting));
		}
		return this;
	}
	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	public KeyMap loadRodContent(final KeyTag tag, final KeyRodType type, final KeyMapSettingType setting){
		if(this.contents.containsKey(tag)){
			this.contents.replace(tag,new KeyMapRodContent(tag,type));
		}else{
			this.contents.put(tag,new KeyMapRodContent(tag,type));
		}
		return this;
	}

	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public KeyMap loadButtonContent(final KeyTag tag, final KeyButtonType type, final KeyMapSettingType setting, final boolean IsGamePad1){
		if(this.contents.containsKey(tag)){
			this.contents.replace(tag,new KeyMapButtonContent(tag,type,setting,IsGamePad1));
		}else{
			this.contents.put(tag,new KeyMapButtonContent(tag,type,setting,IsGamePad1));
		}
		return this;
	}
	/**
	 * 冲突解决：replace
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public KeyMap loadRodContent(final KeyTag tag, final KeyRodType type, final KeyMapSettingType setting, final boolean IsGamePad1){
		if(this.contents.containsKey(tag)){
			this.contents.replace(tag,new KeyMapRodContent(tag,type, IsGamePad1));
		}else{
			this.contents.put(tag,new KeyMapRodContent(tag,type, IsGamePad1));
		}
		return this;
	}

	@ExtractedInterfaces
	public boolean getButtonStateFromTagAndGamePad(final KeyTag tag, @NonNull final BasicIntegrationGamepad gamepad){
		return gamepad.getButtonState(Objects.requireNonNull((KeyMapButtonContent) this.contents.get(tag)).type, Objects.requireNonNull(this.contents.get(tag)).setting);
	}
	@ExtractedInterfaces
	public double getRodStateFromTagAndGamePad(final KeyTag tag, @NonNull final BasicIntegrationGamepad gamepad){
		return gamepad.getRodState(Objects.requireNonNull((KeyMapRodContent) this.contents.get(tag)).type);
	}

	@ExtractedInterfaces
	public boolean IsControlledByGamepad1(final KeyTag tag){
		return Objects.requireNonNull(this.contents.get(tag)).IsControlledByGamePad1;
	}

	@ExtractedInterfaces
	public boolean containsKeySetting(final KeyTag tag){
		return this.contents.containsKey(tag);
	}

	public void showContentInfo(){
		for(final Map.Entry<KeyTag, KeyMapContent> entry: this.contents.entrySet()){
			Global.client.changeData(entry.getKey().name() , entry.getValue().toString());
		}
	}
}
