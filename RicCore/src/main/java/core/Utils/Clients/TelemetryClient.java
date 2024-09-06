package core.Utils.Clients;

import android.util.Pair;

import core.robotcore.external.Telemetry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class TelemetryClient {
	public Telemetry telemetry;
	private final Map < String , Pair< String , Integer >> data;
	private final Map < String , Integer > lines;
	private int ID=0;

	public TelemetryClient(Telemetry telemetry){
		this.telemetry=telemetry;
		data =new HashMap<>();
		lines=new HashMap<>();
		update();
	}
	public void clearInfo(){
		data.clear();
		update();
	}
	public void clearLines(){
		lines.clear();
		update();
	}
	public void clear(){
		clearInfo();
		clearLines();
		update();
	}

	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,String val){
		data.put(key,new Pair<>(val, ++ ID));
		update();
	}
	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,Object val){
		addData(key,String.valueOf(val));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteData(String key){
		if( data.containsKey(key)){
			data.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
		update();
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeData(String key, String val){
		if(data.containsKey(key)){
			data.replace(key,new Pair<>(val, (Objects.requireNonNull(data.get(key))).second));
		}else{
			addData(key, val);
		}
		update();
	}
	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeData(String key, Object val){
		changeData(key,String.valueOf(val));
	}

	public void addLine(String key){
		lines.put(key,++ID);
		update();
	}
	public void addLine(Object key){
		addLine(String.valueOf(key));
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
		update();
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
		update();
	}

	public void update(){
		Vector<Pair<Integer, String>> outputData = new Vector<>();
		for (Map.Entry<String, Pair<String, Integer>> i : data.entrySet() ) {
			String key = i.getKey(),val=i.getValue().first;
			Integer IDCache=i.getValue().second;
			outputData.add(new Pair<>(IDCache,key+":"+val));
		}
		for ( Map.Entry<String, Integer> entry : lines.entrySet() ) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			outputData.add(new Pair<>(val, key));
		}
		outputData.sort(Comparator.comparingInt(x -> x.first));
		for ( Pair<Integer, String> outputLine : outputData ) {
			telemetry.addLine("["+ outputLine.first+"]"+ outputLine.second);
		}
		telemetry.update();
	}
}
