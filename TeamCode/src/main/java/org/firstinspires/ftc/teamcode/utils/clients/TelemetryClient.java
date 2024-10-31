package org.firstinspires.ftc.teamcode.utils.clients;

import android.util.Pair;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Params;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class TelemetryClient {
	public Telemetry telemetry;
	protected final Map < String , Pair< String , Integer >> data;
	protected int ID=0;
	public boolean showIndex=false;
	private static TelemetryClient instanceTelemetryClient;

	public TelemetryClient(Telemetry telemetry){
		this.telemetry=telemetry;
		data = new HashMap<>();
//		update();
		instanceTelemetryClient=this;
	}

	public static TelemetryClient getInstance(){
		return instanceTelemetryClient;
	}

	public void clear(){
		data.clear();
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
		data.remove(key);
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
		data.put(key,new Pair<>("",++ID));
		update();
	}
	public void addLine(Object key){
		addLine(String.valueOf(key));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteLine(String key){
		data.remove(key);
		update();
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	public void changeLine(String oldData, String newData){
		int cache=Objects.requireNonNull(data.get(oldData)).second;
		data.remove(oldData);
		data.put(newData,new Pair<>("",cache));
		update();
	}

	public void update(){
		if(Params.Configs.sortDataInTelemetryClientUpdate) {
			Vector <Pair <Integer, String>> outputData = new Vector <>();
			for (Map.Entry <String, Pair <String, Integer>> i : data.entrySet()) {
				String  key     = i.getKey(), val = i.getValue().first;
				Integer IDCache = i.getValue().second;
				if (! Objects.equals(i.getValue().first, "")) {//line
					outputData.add(new Pair <>(IDCache, key + ":" + val));
					if (Params.Configs.dashboardAutoSyncWithTelemetry) {
						DashboardClient.getInstance().put(key, val);
					}
				} else {//line
					outputData.add(new Pair <>(IDCache, key));
				}
			}
			outputData.sort(Comparator.comparingInt(x -> x.first));
			for (Pair <Integer, String> outputLine : outputData) {
				if (showIndex) {
					telemetry.addLine("[" + outputLine.first + "]" + outputLine.second);
				} else {
					telemetry.addLine(outputLine.second);
				}
			}
			telemetry.update();
		}else{
			String cache;
			for (Map.Entry<String, Pair<String, Integer>> entry : data.entrySet()) {
				String                key = entry.getKey();
				Pair<String, Integer> val = entry.getValue();
				cache= Objects.equals(val.first, "") ? val.first : key + ":" + val.first;
				if (showIndex) {
					telemetry.addLine("[" + val.second + "]" + cache);
				} else {
					telemetry.addLine(key + ":" + cache);
				}
				if(Params.Configs.dashboardAutoSyncWithTelemetry){
					DashboardClient.getInstance().put(key, val);
				}
			}
		}
	}
}
