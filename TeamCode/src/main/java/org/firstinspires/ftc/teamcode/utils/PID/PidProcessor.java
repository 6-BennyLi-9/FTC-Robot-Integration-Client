package org.firstinspires.ftc.teamcode.utils.PID;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;

import java.util.Map;

public class PidProcessor {
	private final PidContentPackage contents;

	public PidProcessor(){
		this.contents =new PidContentPackage();
	}

	private void RationalizeI(@NonNull final PidContent content){
		if(this.contents.TagIsAngleBasedContent(content.tag)){
			content.I= Mathematics.roundClip(content.I,content.MAX_I);
		}else{
			content.I=Mathematics.intervalClip(content.I,-content.MAX_I,content.MAX_I);
		}
	}

	/**
	 * 不要更改，不要更改，不要更改
	 * @param content 要调用那一个数据
	 */
	public void ModifyPID(@NonNull final PidContent content){
		content.P=content.inaccuracies*content.getKp();
		content.I+=content.inaccuracies * content.getKi() * content.timer.getDeltaTime();

		this.RationalizeI(content);

		content.D=(content.inaccuracies-content.lastInaccuracies)* content.getKd()/ content.timer.getDeltaTime();
		content.lastInaccuracies=content.inaccuracies;

		content.fulfillment=content.getFulfillment();
	}

	/**
	 * 刷新所有PID
	 */
	public void update(){
		PidContent content;
		for (final Map.Entry<String, PidContent> entry : this.contents.coreContents.entrySet()) {
			content = entry.getValue();
			content.timer.stopAndRestart();

			this.ModifyPID(content);
		}
	}

	/**
	 * @param content 将要加入的content
	 */
	@UtilFunctions
	public void loadContent(final PidContent content){
		this.contents.register(content);
	}

	/**
	 * 自动抑制可能报出的错误
	 * @param tag Content tag
	 * @param inaccuracies 登记的误差，不会自动update()
	 */
	public void registerInaccuracies(final String tag, final double inaccuracies){
		try {
			this.contents.getTag(tag).inaccuracies=inaccuracies;
		} catch (final ClassNotFoundException ignored) {}
	}

	/**
	 * 自动抑制可能报出的错误
	 * @return 如果发生错误，则返回0
	 */
	public double getFulfillment(final String Tag){
		try {
			return this.contents.getTag(Tag).fulfillment;
		} catch (final ClassNotFoundException ignored) {}
		return 0;
	}
	public void ModifyPidByTag(final String tag){
		try {
			this.ModifyPID(this.contents.getTag(tag));
		} catch (final ClassNotFoundException ignored) {}
	}

	@NonNull
	@Override
	public String toString() {
		return this.contents.toString();
	}
}
