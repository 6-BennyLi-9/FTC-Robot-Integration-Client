package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.keymap.KeyMapButtonContent;
import org.firstinspires.ftc.teamcode.keymap.KeyMapContent;
import org.firstinspires.ftc.teamcode.keymap.KeyMapRodContent;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

import java.util.Map;

@TeleOp(name = "KeyMapTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class KeyMapTest extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		for(final Map.Entry<KeyTag, KeyMapContent> entry : this.robot.gamepad.keyMap.contents.entrySet()){
			if(entry.getValue() instanceof KeyMapButtonContent){
				this.robot.client.changeData(entry.getValue().tag.name(), this.robot.gamepad.getButtonRunAble(entry.getKey()));
			}else if(entry.getValue() instanceof KeyMapRodContent){
				this.robot.client.changeData(entry.getValue().tag.name(), this.robot.gamepad.getRodState(entry.getKey()));
			}
		}
	}

	@Override
	public void whenInit() {
		this.robot.registerGamepad(this.gamepad1, this.gamepad2);
		this.robot.gamepad.keyMap =new KeyMap();

		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.SinglePressToChangeRunAble);
		this.robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod);
		this.robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod);
		this.robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.ChassisSpeedConfig, KeyButtonType.X, KeyMapSettingType.SinglePressToChangeRunAble);
	}
}
