package org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyButtonType;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.Utils.Enums.KeyRodType;

import java.util.HashMap;
import java.util.Map;

public class BasicIntegrationGamepad{
	public final Gamepad gamepad;
	public final Map<KeyButtonType,Boolean> LastState;

	public BasicIntegrationGamepad(Gamepad gamepad){
		this.gamepad=gamepad;
		LastState=new HashMap<>();
	}

	@UserRequirementFunctions
	public boolean A(){return gamepad.a;}
	@UserRequirementFunctions
	public boolean B(){return gamepad.b;}
	@UserRequirementFunctions
	public boolean X(){return gamepad.x;}
	@UserRequirementFunctions
	public boolean Y(){return gamepad.y;}

	@UserRequirementFunctions
	public boolean DpadUp(){return gamepad.dpad_up;}
	@UserRequirementFunctions
	public boolean DpadDown(){return gamepad.dpad_down;}
	@UserRequirementFunctions
	public boolean DpadLeft(){return gamepad.dpad_left;}
	@UserRequirementFunctions
	public boolean DpadRight(){return gamepad.dpad_right;}

	@UserRequirementFunctions
	public double LeftStickX(){return gamepad.left_stick_x;}
	@UserRequirementFunctions
	public double LeftStickY(){return gamepad.left_stick_y;}
	@UserRequirementFunctions
	public double RightStickX(){return gamepad.right_stick_x;}
	@UserRequirementFunctions
	public double RightStickY(){return gamepad.right_stick_y;}

	//TODO:添加需求Function如果需要

	@UserRequirementFunctions
	public boolean getCurrentButtonState(@NonNull KeyButtonType type){
		boolean res=false;
		switch (type) {
			case A:
				res=A();
				break;
			case B:
				res=B();
				break;
			case X:
				res=X();
				break;
			case Y:
				res=Y();
				break;
			case DpadUp:
				res=DpadUp();
				break;
			case DpadDown:
				res=DpadDown();
				break;
			case DpadLeft:
				res=DpadLeft();
				break;
			case DpadRight:
				res=DpadRight();
				break;
		}
		return Boolean.TRUE.equals(LastState.put(type, res));
	}
	@UserRequirementFunctions
	public boolean getButtonState(@NonNull KeyButtonType type, @NonNull KeyMapSettingType setting){
		boolean lst= Boolean.TRUE.equals(LastState.get(type));
		boolean now= getCurrentButtonState(type);
		boolean res=false;

		switch (setting) {
			case RunWhenButtonPressed:
				res=now&&!lst;
				break;
			case RunWhenButtonPressingBooleanChanged:
				res=lst!=now;
				break;
			case RunWhenButtonHold:
				res=now;
				break;
			case PullRod:
				throw new RuntimeException("Cannot Get The STATE Of A PullRod");
		}

		return res;
	}
	@UserRequirementFunctions
	public double getRodState(@NonNull KeyRodType type){
		double res=0;
		switch (type) {
			case LeftStickX:
				res=LeftStickX();
				break;
			case LeftStickY:
				res=LeftStickY();
				break;
			case RightStickX:
				res=RightStickX();
				break;
			case RightStickY:
				res=RightStickY();
				break;
		}
		return res;
	}
}
