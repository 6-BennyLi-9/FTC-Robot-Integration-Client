package org.firstinspires.ftc.teamcode.utils.PID;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;

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

	/**
	 * 自动抑制可能报出的错误
	 * @param tag Content Tag
	 * @param inaccuracies 登记的误差，不会自动update()
	 */
	public void registerInaccuracies(String tag,double inaccuracies){
		try {
			contents.getTag(tag).inaccuracies=inaccuracies;
		} catch (ClassNotFoundException ignored) {}
	}

	/**
	 * 自动抑制可能报出的错误
	 * @return 如果发生错误，则返回0
	 */
	public double getFulfillment(String Tag){
		try {
			return contents.getTag(Tag).fulfillment;
		} catch (ClassNotFoundException ignored) {}
		return 0;
	}
	public void ModifyPidByTag(String tag){
		try {
			ModifyPID(contents.getTag(tag));
		} catch (ClassNotFoundException ignored) {}
	}
}
