package org.firstinspires.ftc.teamcode.Utils.PID;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Mathematics;

import java.util.Map;

public class PidProcessor {
	private final PidContentPackage contents;

	public PidProcessor(){
		contents=new PidContentPackage();
	}

	private void RationalizeI(@NonNull PidContent content){
		if(contents.TagIsAngleBasedContent(content.Tag)){
			content.I=Mathematics.roundClip(content.I,content.MAX_I);
		}else{
			content.I=Mathematics.intervalClip(content.I,-content.MAX_I,content.MAX_I);
		}
	}

	/**
	 * 不要更改，不要更改，不要更改
	 * @param content 要调用那一个数据
	 */
	public void ModifyPID(@NonNull PidContent content){
		content.P=content.inaccuracies*content.getKp();
		content.I+=content.inaccuracies * content.getKi() * content.timer.getDeltaTime();

		RationalizeI(content);

		content.D=(content.inaccuracies-content.lastInaccuracies)* content.getKd()/ content.timer.getDeltaTime();
		content.lastInaccuracies=content.inaccuracies;

		content.fulfillment=content.getFulfillment();
	}

	/**
	 * 刷新所有PID
	 */
	public void update(){
		PidContent content;
		for (Map.Entry<String, PidContent> entry : contents.coreContents.entrySet()) {
			content = entry.getValue();
			content.timer.stopAndRestart();

			ModifyPID(content);
		}
	}

	/**
	 * @param content 将要加入的content
	 */
	@UtilFunctions
	public void loadContent(PidContent content){
		contents.register(content);
	}
}
