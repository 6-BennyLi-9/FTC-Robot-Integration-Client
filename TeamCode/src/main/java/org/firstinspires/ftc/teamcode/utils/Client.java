package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

/**
 * @see org.firstinspires.ftc.teamcode.RIC_samples.ClientUsage
 */
public class Client {
	Telemetry telemetry;
	private final Map < String , Telemetry.Item > items;
	private final Map < String , Telemetry.Line > lines;
	private Telemetry.Item itemCache;
	private Telemetry.Line lineCache;

	public Client(Telemetry telemetry){
		this.telemetry=telemetry;
		items=new HashMap<>();
		lines=new HashMap<>();
	}

	public void clearInfo(){
		items.forEach((key,val)->telemetry.removeItem(val));
		items.clear();
	}
	public void clearLines(){
		lines.forEach((key,val)->telemetry.removeLine(val));
		lines.clear();
	}
	public void clear(){
		clearInfo();
		clearLines();
	}

	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,String val){
		itemCache =telemetry.addData(key,val);
		items.put(key, itemCache);
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteDate(String key){
		if(items.containsKey(key)){
			itemCache =items.get(key);
			telemetry.removeItem(itemCache);
			items.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeDate(String key,String val){
		if(items.containsKey(key)){
			itemCache =items.get(key);
			assert itemCache != null;//这只是为了安抚AS的过于严格的代码检查而已，实际并无作用
			itemCache.setValue(val);
		}else{
			addData(key, val);
		}
	}

	public void addLine(String key){
		lineCache=telemetry.addLine(key);
		lines.put(key,lineCache);
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteLine(String key){
		if(lines.containsKey(key)){
			lineCache=lines.get(key);
			telemetry.removeLine(lineCache);
			items.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 * @param key 目标行
	 * @param val 替换行
	 */
	public void changeLine(String key,String val){
		if(lines.containsKey(key)){
			deleteLine(key);
			addLine(val);
		}else{
			addLine(key);
		}
	}
}
