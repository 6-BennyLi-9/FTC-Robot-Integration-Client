package org.firstinspires.ftc.teamcode.hardwares.integration;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.BasicIntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.keymap.KeyMapButtonContent;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 集成化的gamepad（有俩）
 *
 * @see BasicIntegrationGamepad
 */
public class IntegrationGamepad {
	public BasicIntegrationGamepad gamepad1,gamepad2;
	public KeyMap keyMap;

	@UserRequirementFunctions
	public IntegrationGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		this(new BasicIntegrationGamepad(gamepad1),new BasicIntegrationGamepad(gamepad2));
	}
	public IntegrationGamepad(final BasicIntegrationGamepad gamepad1, final BasicIntegrationGamepad gamepad2){
		this.gamepad1=gamepad1;
		this.gamepad2=gamepad2;
		this.keyMap =new KeyMap();
		this.keyMap.initKeys();
	}

	@UserRequirementFunctions
	@Deprecated
	public void swap(){
		final BasicIntegrationGamepad tmp = this.gamepad1;
		this.gamepad1 = this.gamepad2;
		this.gamepad2 =tmp;
	}

	@UserRequirementFunctions
	public boolean getButtonRunAble(final KeyTag tag){
		final KeyMapButtonContent cache = (KeyMapButtonContent) this.keyMap.contents.get(tag);
		if(this.keyMap.IsControlledByGamepad1(tag)){
			assert null != cache;
			return this.gamepad1.getButtonState(cache.type,cache.setting);
		}else {
			assert null != cache;
			return this.gamepad2.getButtonState(cache.type,cache.setting);
		}
	}
	@UserRequirementFunctions
	public double getRodState(final KeyTag tag){
		if(this.keyMap.IsControlledByGamepad1(tag)){
			return this.keyMap.getRodStateFromTagAndGamePad(tag, this.gamepad1);
		}else {
			return this.keyMap.getRodStateFromTagAndGamePad(tag, this.gamepad2);
		}
	}

	@ExtractedInterfaces
	public void showContentInfo(){
		this.keyMap.showContentInfo();
	}
	@ExtractedInterfaces
	public void showLst(){
		this.gamepad1.showLst("gamepad1");
		this.gamepad2.showLst("gamepad2");
	}
}
