package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.Enums.KeyButtonType.*;
import static org.firstinspires.ftc.teamcode.Utils.Enums.KeyMapSettingType.*;
import static org.firstinspires.ftc.teamcode.Utils.Enums.KeyRodType.*;
import static org.firstinspires.ftc.teamcode.Utils.Enums.KeyTag.*;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.BasicIntegrationGamepad;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyButtonType;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyRodType;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

abstract class KeyMapContent{
	public final boolean IsControlledByGamePad1;
	public final KeyTag tag;
	public final KeyMapSettingType setting;
	public KeyMapContent(KeyTag tag,KeyMapSettingType setting){
		this(tag,setting,true);
	}
	public KeyMapContent(KeyTag tag,KeyMapSettingType setting,boolean IsControlledByGamePad1){
		this.tag=tag;
		this.setting=setting;
		this.IsControlledByGamePad1 = IsControlledByGamePad1;
	}
}
class KeyMapButtonContent extends KeyMapContent{
	public final KeyButtonType type;
	public KeyMapButtonContent(KeyTag tag,KeyButtonType type, KeyMapSettingType setting) {
		super(tag, setting);
		this.type=type;
	}
	public KeyMapButtonContent(KeyTag tag,KeyButtonType type, KeyMapSettingType setting,boolean IsControlledByGamePad1) {
		super(tag, setting, IsControlledByGamePad1);
		this.type=type;
	}
}
class KeyMapRodContent extends KeyMapContent{
	public final KeyRodType type;
	public KeyMapRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting) {
		super(tag, setting);
		this.type=type;
	}
	public KeyMapRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting,boolean IsControlledByGamePad1) {
		super(tag, setting, IsControlledByGamePad1);
		this.type=type;
	}
}

public final class KeyMap {
	private final Map<KeyTag, KeyMapContent> contents;
	public KeyMap(){
		contents=new HashMap<>();
		init();
	}

	public void init(){
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

	@UserRequirementFunctions
	public void loadButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting){
		contents.put(tag,new KeyMapButtonContent(tag,type,setting));
	}
	@UserRequirementFunctions
	public void loadRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting){
		contents.put(tag,new KeyMapRodContent(tag,type,setting));
	}

	@UserRequirementFunctions
	@ExtractedInterfaces
	public void loadButtonContent(KeyTag tag, KeyButtonType type, KeyMapSettingType setting,boolean IsGamePad1){
		contents.put(tag,new KeyMapButtonContent(tag,type,setting,IsGamePad1));
	}
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void loadRodContent(KeyTag tag, KeyRodType type, KeyMapSettingType setting,boolean IsGamePad1){
		contents.put(tag,new KeyMapRodContent(tag,type,setting,IsGamePad1));
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
	public boolean ContainsKeySetting(KeyTag tag){
		return contents.containsKey(tag);
	}
}
