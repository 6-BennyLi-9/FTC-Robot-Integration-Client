package org.firstinspires.ftc.teamcode.hardwares.integration;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.BasicIntegrationGamepad;
import org.firstinspires.ftc.teamcode.KeyMap;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

public class IntegrationGamepad {
	public BasicIntegrationGamepad gamepad1,gamepad2;
	public KeyMap keyMap;

	@UserRequirementFunctions
	public IntegrationGamepad(Gamepad gamepad1,Gamepad gamepad2){
		this(new BasicIntegrationGamepad(gamepad1),new BasicIntegrationGamepad(gamepad2));
	}
	public IntegrationGamepad(BasicIntegrationGamepad gamepad1,BasicIntegrationGamepad gamepad2){
		this.gamepad1=gamepad1;
		this.gamepad2=gamepad2;
		keyMap =new KeyMap();
		keyMap.initKeys();
	}

	@UserRequirementFunctions
	@Deprecated
	public void swap(){
		BasicIntegrationGamepad tmp=gamepad1;
		gamepad1=gamepad2;
		gamepad2=tmp;
	}

	@UserRequirementFunctions
	public boolean getButtonRunAble(KeyTag tag){
		if(keyMap.IsControlledByGamepad1(tag)){
			return keyMap.getButtonStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return keyMap.getButtonStateFromTagAndGamePad(tag,gamepad2);
		}
	}
	@UserRequirementFunctions
	public double getRodState(KeyTag tag){
		if(keyMap.IsControlledByGamepad1(tag)){
			return keyMap.getRodStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return keyMap.getRodStateFromTagAndGamePad(tag,gamepad2);
		}
	}
}
