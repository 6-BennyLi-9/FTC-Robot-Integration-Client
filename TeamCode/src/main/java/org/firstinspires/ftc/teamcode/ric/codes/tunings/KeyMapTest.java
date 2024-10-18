package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMapButtonContent;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMapContent;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMapRodContent;

import java.util.Map;

@TeleOp(name = "KeyMapTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class KeyMapTest extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		for(Map.Entry<KeyTag, KeyMapContent> entry : robot.gamepad.keyMap.contents.entrySet()){
			if(entry.getValue() instanceof KeyMapButtonContent){
				robot.client.changeData(entry.getValue().tag.name(),robot.gamepad.getButtonRunAble(entry.getKey()));
			}else if(entry.getValue() instanceof KeyMapRodContent){
				robot.client.changeData(entry.getValue().tag.name(),robot.gamepad.getRodState(entry.getKey()));
			}
		}
	}

	@Override
	public void whenInit() {
		robot.registerGamepad(gamepad1,gamepad2);
		robot.gamepad.keyMap =new KeyMap();

		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.SinglePressToChangeRunAble);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.ChassisSpeedConfig, KeyButtonType.X, KeyMapSettingType.SinglePressToChangeRunAble);
	}
}
