package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.BasicIntegrationGamepad;
import org.firstinspires.ftc.teamcode.KeyMap;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyTag;

public class IntegrationGamepad {
	public BasicIntegrationGamepad gamepad1,gamepad2;
	public final KeyMap map;

	@UserRequirementFunctions
	public IntegrationGamepad(Gamepad gamepad1,Gamepad gamepad2){
		this(new BasicIntegrationGamepad(gamepad1),new BasicIntegrationGamepad(gamepad2));
	}
	public IntegrationGamepad(BasicIntegrationGamepad gamepad1,BasicIntegrationGamepad gamepad2){
		this.gamepad1=gamepad1;
		this.gamepad2=gamepad2;
		map=new KeyMap();
	}

	@UserRequirementFunctions
	public void swap(){
		BasicIntegrationGamepad tmp=gamepad1;
		gamepad1=gamepad2;
		gamepad2=tmp;
	}

	@UserRequirementFunctions
	public boolean getButtonRunAble(KeyTag tag){
		if(map.IsControlledByGamepad1(tag)){
			return map.getButtonStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return map.getButtonStateFromTagAndGamePad(tag,gamepad2);
		}
	}
	@UserRequirementFunctions
	public double getRodState(KeyTag tag){
		if(map.IsControlledByGamepad1(tag)){
			return map.getRodStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return map.getRodStateFromTagAndGamePad(tag,gamepad2);
		}
	}
}
