package org.firstinspires.ftc.teamcode.hardwares.integration.gamepads;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

import java.util.HashMap;
import java.util.Map;

public class BasicIntegrationGamepad{
	public final Gamepad gamepad;
	public final Map<KeyButtonType,Boolean> LastState;
	private boolean flag;

	public BasicIntegrationGamepad(final Gamepad gamepad){
		this.gamepad=gamepad;
		this.LastState =new HashMap<>();
	}

	@UserRequirementFunctions
	public boolean A(){return this.gamepad.a;}
	@UserRequirementFunctions
	public boolean B(){return this.gamepad.b;}
	@UserRequirementFunctions
	public boolean X(){return this.gamepad.x;}
	@UserRequirementFunctions
	public boolean Y(){return this.gamepad.y;}

	@UserRequirementFunctions
	public boolean DpadUp(){return this.gamepad.dpad_up;}
	@UserRequirementFunctions
	public boolean DpadDown(){return this.gamepad.dpad_down;}
	@UserRequirementFunctions
	public boolean DpadLeft(){return this.gamepad.dpad_left;}
	@UserRequirementFunctions
	public boolean DpadRight(){return this.gamepad.dpad_right;}

	@UserRequirementFunctions
	public double LeftStickX(){return this.gamepad.left_stick_x;}
	@UserRequirementFunctions
	public double LeftStickY(){return this.gamepad.left_stick_y;}
	@UserRequirementFunctions
	public double RightStickX(){return this.gamepad.right_stick_x;}
	@UserRequirementFunctions
	public double RightStickY(){return this.gamepad.right_stick_y;}

	//TODO:添加需求Function如果需要

	@UserRequirementFunctions
	public boolean getCurrentButtonState(@NonNull final KeyButtonType type){
		boolean res=false;
		switch (type) {
			case A:
				res= this.A();
				break;
			case B:
				res= this.B();
				break;
			case X:
				res= this.X();
				break;
			case Y:
				res= this.Y();
				break;
			case DpadUp:
				res= this.DpadUp();
				break;
			case DpadDown:
				res= this.DpadDown();
				break;
			case DpadLeft:
				res= this.DpadLeft();
				break;
			case DpadRight:
				res= this.DpadRight();
				break;
		}
		return res;
	}
	@UserRequirementFunctions
	public boolean getButtonState(@NonNull final KeyButtonType type, @NonNull final KeyMapSettingType setting){
		if(! this.LastState.containsKey(type)){
			this.LastState.put(type,false);
		}
		final boolean lst = Boolean.TRUE.equals(this.LastState.get(type));
		final boolean now = this.getCurrentButtonState(type);
		boolean       res =false;

		Global.client.changeData("getButtonState-params",lst+","+now);

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
			case SinglePressToChangeRunAble:
				this.flag = (lst == now) == this.flag;
				res= this.flag;
				break;
			case PullRod:
				throw new RuntimeException("Cannot Get The STATE Of A PullRod");
		}

		this.LastState.put(type,now);
		return res;
	}
	@UserRequirementFunctions
	public double getRodState(@NonNull final KeyRodType type){
		double res=0;
		switch (type) {
			case LeftStickX:
				res= this.LeftStickX();
				break;
			case LeftStickY:
				res= this.LeftStickY();
				break;
			case RightStickX:
				res= this.RightStickX();
				break;
			case RightStickY:
				res= this.RightStickY();
				break;
		}
		return res;
	}

	public void showLst(final String enterCode){
		for(final Map.Entry<KeyButtonType,Boolean> entry: this.LastState.entrySet()){
			Global.client.changeData("["+enterCode+"]"+entry.getKey().name(),entry.getValue());
		}
	}
}
