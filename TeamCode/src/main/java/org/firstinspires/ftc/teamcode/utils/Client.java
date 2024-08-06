package org.firstinspires.ftc.teamcode.utils;

import android.util.Pair;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @see org.firstinspires.ftc.teamcode.RIC_samples.ClientUsage
 */
public class Client {
	Telemetry telemetry;
	private final Map < String , Pair < String , Integer > > data;
	private final Map < String , Integer > lines;
	private int ID=0;

	public Client(Telemetry telemetry){
		this.telemetry=telemetry;
		data =new HashMap<>();
		lines=new HashMap<>();
	}

	public void clearInfo(){
		data.clear();
	}
	public void clearLines(){
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
		data.put(key,new Pair<>(val, ++ ID));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteDate(String key){
		if( data.containsKey(key)){
			data.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeDate(String key,String val){
		if( data.containsKey(key)){
			data.replace(key,new Pair<>(val, ++ ID));
		}else{
			addData(key, val);
		}
	}

	public void addLine(String key){
		lines.put(key,++ID);
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteLine(String key){
		if(lines.containsKey(key)){
			data.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 * @param oldDate 目标行
	 * @param newData 替换行
	 */
	public void changeLine(String oldDate, String newData){
		if(lines.containsKey(oldDate)){
			int cache=Objects.requireNonNull(lines.get(oldDate));
			lines.remove(oldDate);
			lines.put(newData,cache);
		}else{
			addLine(newData);
		}
	}

	public void update(){

	}
}
